package org.springframework.nanotrader.data.util;

import org.json.JSONObject;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import org.springframework.nanotrader.data.domain.MarketSummary;
import java.math.BigDecimal;

public class CurrencyUtils {

  public static MarketSummary convertCurrency(MarketSummary marketSummary, Locale locale) {

    BigDecimal tradeStockIndexAverage = marketSummary.getTradeStockIndexAverage();
    BigDecimal tradeStockIndexOpenAverage = marketSummary.getTradeStockIndexOpenAverage();


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

    BigDecimal exchangeRate = findExchangeRate(locale, rates);

    marketSummary.setTradeStockIndexAverage((tradeStockIndexAverage.multiply(exchangeRate))
        .setScale(FinancialUtils.SCALE, FinancialUtils.ROUND));
    marketSummary.setTradeStockIndexOpenAverage((tradeStockIndexOpenAverage.multiply(exchangeRate))
        .setScale(FinancialUtils.SCALE, FinancialUtils.ROUND));

    return marketSummary;
  }

  public static BigDecimal findExchangeRate(Locale locale, JSONObject rates) {
    return BigDecimal.valueOf(rates.getDouble(Currency.getInstance(locale).getCurrencyCode()));
  }

}
