package org.springframework.nanotrader.data.util;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.*;
import java.io.*;
import java.util.*;
import org.springframework.nanotrader.data.domain.MarketSummary;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtils {
  private static JSONObject rates = null;

  private static void loadRates() throws IOException {
    URL url = new URL("https://api.exchangerate-api.com/v4/latest/USD");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.setConnectTimeout(5000);
    con.setReadTimeout(5000);
    JSONTokener tokener = new JSONTokener(con.getInputStream());
    JSONObject obj = new JSONObject(tokener);
    rates = obj.getJSONObject("rates");
  }

  private static BigDecimal getRate(String currencyCode) throws IOException {
    if(rates == null) {
      rates = new JSONObject();
      loadRates();
    }

    return BigDecimal.valueOf(rates.getDouble(currencyCode));
  }

  public static MarketSummary convertCurrency(MarketSummary marketSummary, Locale locale) {

    BigDecimal tradeStockIndexAverage = marketSummary.getTradeStockIndexAverage();
    BigDecimal tradeStockIndexOpenAverage = marketSummary.getTradeStockIndexOpenAverage();

    String currencyCode = Currency.getInstance(locale).getCurrencyCode();
    try {
      BigDecimal exchangeRate = getRate(currencyCode);

      marketSummary.setTradeStockIndexAverage((tradeStockIndexAverage.multiply(exchangeRate))
          .setScale(FinancialUtils.SCALE, RoundingMode.HALF_UP));
      marketSummary.setTradeStockIndexOpenAverage((tradeStockIndexOpenAverage.multiply(exchangeRate))
          .setScale(FinancialUtils.SCALE, RoundingMode.HALF_UP));
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }

    return marketSummary;
  }

}
