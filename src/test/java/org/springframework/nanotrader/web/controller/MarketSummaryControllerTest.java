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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 *  MarketSummaryControllerTest tests the MarketSummary REST api
 *  
 *  @author Brian Dussault 
 *  @author
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MarketSummaryControllerTest {

	public static BigDecimal MARKET_INDEX =  	BigDecimal.valueOf(104.77);
	public static BigDecimal MARKET_OPENING =   BigDecimal.valueOf(104.45);
	public static BigDecimal MARKET_VOLUME =   	BigDecimal.valueOf(0);

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getMarketSummaryJson() throws Exception {
		mockMvc.perform(get("/marketSummary").header("Accept-Language", "en-US").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.tradeStockIndexAverage").value(MARKET_INDEX.doubleValue()))
				.andExpect(jsonPath("$.tradeStockIndexOpenAverage").value(MARKET_OPENING.doubleValue()))
				.andExpect(jsonPath("$.tradeStockIndexVolume").value(MARKET_VOLUME.doubleValue()))
				.andExpect(jsonPath("$.percentGain").value(new BigDecimal(0).doubleValue()))
				.andDo(print());
	}
  
}
