/*
* Copyright 2002-2012 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springframework.nanotrader.service.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.nanotrader.data.domain.MarketSummary;
import org.springframework.nanotrader.data.domain.Quote;
import org.springframework.nanotrader.data.repository.MarketSummaryRepository;
import org.springframework.nanotrader.data.repository.QuoteRepository;
import org.springframework.stereotype.Service;

/**
 * Facade that, generally, delegates directly to a {@link TradingService}, after
 * mapping from service domain to data domain. For
 * {@link #saveOrder(Order, boolean)}, and option for synch/asynch processing is
 * provided.
 * 
 * @author Gary Russell
 * @author Brian Dussault
 * @author Kashyap Parikh
 */
@Service
public class TradingServiceFacadeImpl implements TradingServiceFacade {

  private static Integer TOP_N = 3;

  private static Logger log = LoggerFactory.getLogger(TradingServiceFacadeImpl.class);

  @Autowired
  private QuoteRepository quoteRepository;

  @Autowired
  private MarketSummaryRepository marketSummaryRepository;

  public MarketSummary findMarketSummary() {
    
    if (log.isDebugEnabled()) {
     log.debug("TradingServiceFacade.findMarketSummary: Start");
    }
    MarketSummary marketSummary = marketSummaryRepository.findMarketSummary();
    // get top losing stocks
    Page<Quote> losers = quoteRepository.findAll(new PageRequest(0, TOP_N, new Sort(Direction.ASC, "change1")));

    // get top gaining stocks
    Page<Quote> winners = quoteRepository.findAll(new PageRequest(0, TOP_N, new Sort(Direction.DESC, "change1")));

    List<Quote> topLosers = new ArrayList<Quote>(TOP_N);
    for (Quote q : losers) {
      topLosers.add(q);
    }
    List<Quote> topGainers = new ArrayList<Quote>(TOP_N);
    for (Quote q : winners) {
      topGainers.add(q);
    }
    marketSummary.setTopLosers(topLosers);
    marketSummary.setTopGainers(topGainers);
    marketSummary.setSummaryDate(new Date());

    if (log.isDebugEnabled()) {
      log.debug("TradingServiceFacade.findMarketSummary: completed successfully.");
    }
    return marketSummary;
  }

}
