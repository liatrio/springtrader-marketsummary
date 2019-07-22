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
package org.springframework.nanotrader.web.controller;

import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.nanotrader.data.domain.MarketSummary;
import org.springframework.nanotrader.data.util.CurrencyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Provides JSON based REST api to Market Summary
 * 
 * @author Brian Dussault
 */
@Validated
@Controller
public class MarketSummaryController extends BaseController {

  @RequestMapping(value = "/marketSummary", method = RequestMethod.GET)
  public ResponseEntity<MarketSummary> findMarketSummary(@HasCurrency Locale locale) {
    return new ResponseEntity<MarketSummary>(
        CurrencyUtils.convertCurrency(getTradingServiceFacade().findMarketSummary(), locale),
        getNoCacheHeaders(),
        HttpStatus.OK
    );
  }

  @RequestMapping(value = "/marketSummary", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public void post() {
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handle(ConstraintViolationException exception) {
    return exception.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining());
  }
}
