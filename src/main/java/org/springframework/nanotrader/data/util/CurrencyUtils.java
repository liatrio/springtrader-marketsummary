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

    String country = locale.getDisplayCountry();

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


    BigDecimal exchangeRate = findExchangeRate(country);

    marketSummary.setTradeStockIndexAverage((tradeStockIndexAverage.multiply(exchangeRate))
        .setScale(FinancialUtils.SCALE, FinancialUtils.ROUND));
    marketSummary.setTradeStockIndexOpenAverage((tradeStockIndexOpenAverage.multiply(exchangeRate))
        .setScale(FinancialUtils.SCALE, FinancialUtils.ROUND));

    return marketSummary;
  }

  public static BigDecimal findExchangeRate(String country) {

    BigDecimal exchangeRate = new BigDecimal(1.0);
    //BigDecimal exchangeRate;
    
    switch(country) {
      case "United States":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("USD"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("AED"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("ARS"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("AUD"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("BGN"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("BRL"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("BSD"));
        break;
      case "Canada":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CAD"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CHF"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CLP"));
        break;
      case "China":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CNY"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("COP"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CZK"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("DKK"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("DOP"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EGP"));
        break;
      case "Germany":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "Italy":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "France":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("FJD"));
        break;
      case "United Kingdom":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("GBP"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("GTQ"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("HKD"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("HRK"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("HUF"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("IDR"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("ILS"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("INR"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("ISK"));
        break;
      case "Japan":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("JPY"));
        break;
      case "Korea":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("KRW"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("KZT"));
        break;
      case "Mexico":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("MXN"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("MYR"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("NOK"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("NZD"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PAB"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PEN"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PHP"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PKR"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PLN"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PYG"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("RON"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("RUB"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("SAR"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("SEK"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("SGD"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("THB"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("TRY"));
        break;
      case "Taiwan":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("TWD"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("UAH"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("UYU"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("VND"));
        break;
      case "":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("ZAR"));
        break;
    }

    return exchangeRate;
  }
	
}
