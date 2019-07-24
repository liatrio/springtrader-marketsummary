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

import org.json.JSONObject;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.annotation.Resource;
import javax.json.*;

import java.math.BigDecimal;
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
import org.springframework.nanotrader.data.util.FinancialUtils;
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

  public MarketSummary findMarketSummary(Locale locale) {
    
    if (log.isDebugEnabled()) {
     log.debug("TradingServiceFacade.findMarketSummary: Start");
    }
    MarketSummary marketSummary = marketSummaryRepository.findMarketSummary();
    convertCurrency(marketSummary, locale);
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

  public void convertCurrency(MarketSummary marketSummary, Locale locale) {

    String country = locale.getCountry();

    BigDecimal tradeStockIndexAverage = marketSummary.getTradeStockIndexAverage();
    BigDecimal tradeStockIndexOpenAverage = marketSummary.getTradeStockIndexOpenAverage();

    //BigDecimal exchangeRate = new BigDecimal(1.0);
    BigDecimal exchangeRate = null;

    String content = "";
    JSONObject rates = null;
    
    try {
      URL url = new URL("https://api.exchangerate-api.com/v4/latest/USD");
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      BufferedReader in = new BufferedReader(
          new InputStreamReader(con.getInputStream())
        );
      String inputLine;
      StringBuffer contentBuffer = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        contentBuffer.append(inputLine);
      }
      in.close();

      content = contentBuffer.toString();
      JSONObject obj = new JSONObject(content);
      rates = obj.getJSONObject("rates");

    } catch(Exception e) {}



    System.out.println(country);
    switch(country) {
      case "CANADA":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CAD"));
        break;
      case "CANADA_FRENCH":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CAD"));
        break;
      case "CHINA":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CNY"));
        break;
      case "CHINESE":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CNY"));
        break;
      case "ENGLISH":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("GBP"));
        break;
      case "FRANCE":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "FRENCH":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "DE":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "GERMAN":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "GERMANY":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "ITALIAN":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "ITALY":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "JAPAN":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("JPY"));
        break;
      case "JAPANESE":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("JPY"));
        break;
      case "KOREA":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("KRW"));
        break;
      case "KOREAN":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("KRW"));
        break;
      case "PRC":
        exchangeRate = BigDecimal.valueOf(1.0);
        break;
      case "PRIVATE_USE_EXTENSION":
        exchangeRate = BigDecimal.valueOf(1.0);
        break;
      case "ROOT":
        exchangeRate = BigDecimal.valueOf(1.0);
        break;
      case "SIMPLIFIED_CHINESE":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CNY"));
        break;
      case "TAIWAN":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("TWD"));
        break;
      case "UK":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("GBP"));
        break;
      case "UNICODE_LOCALE_EXTENSION":
        exchangeRate = BigDecimal.valueOf(1.0);
        break;
      case "US":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("USD"));
        break;
    }

    marketSummary.setTradeStockIndexAverage((tradeStockIndexAverage.multiply(exchangeRate))
        .setScale(FinancialUtils.SCALE, FinancialUtils.ROUND));
    marketSummary.setTradeStockIndexOpenAverage((tradeStockIndexOpenAverage.multiply(exchangeRate))
        .setScale(FinancialUtils.SCALE, FinancialUtils.ROUND));
  }
}
