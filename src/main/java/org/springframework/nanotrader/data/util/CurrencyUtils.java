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


    BigDecimal exchangeRate = findExchangeRate(country, rates);

    marketSummary.setTradeStockIndexAverage((tradeStockIndexAverage.multiply(exchangeRate))
        .setScale(FinancialUtils.SCALE, FinancialUtils.ROUND));
    marketSummary.setTradeStockIndexOpenAverage((tradeStockIndexOpenAverage.multiply(exchangeRate))
        .setScale(FinancialUtils.SCALE, FinancialUtils.ROUND));

    return marketSummary;
  }

  public static BigDecimal findExchangeRate(String country, JSONObject rates) {

    BigDecimal exchangeRate = null;

    System.out.println(country);

    switch(country) {
      case "United States":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("USD"));
        break;
      case "United Arab Emirates":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("AED"));
        break;
      case "Argentina":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("ARS"));
        break;
      case "Australia":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("AUD"));
        break;
      case "Bulgaria":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("BGN"));
        break;
      case "Brazil":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("BRL"));
        break;
      case "Bahamas":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("BSD"));
        break;
      case "Canada":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CAD"));
        break;
      case "Switzerland":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CHF"));
        break;
      case "Chile":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CLP"));
        break;
      case "China":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CNY"));
        break;
      case "Colombia":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("COP"));
        break;
      case "Czech Republic":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("CZK"));
        break;
      case "Denmark":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("DKK"));
        break;
      case "Dominican Republic":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("DOP"));
        break;
      case "Egypt":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EGP"));
        break;
      case "France":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("EUR"));
        break;
      case "Fiji":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("FJD"));
        break;
      case "United Kingdom":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("GBP"));
        break;
      case "Guatemala":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("GTQ"));
        break;
      case "Hong Kong":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("HKD"));
        break;
      case "Croatia":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("HRK"));
        break;
      case "Hungary":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("HUF"));
        break;
      case "Indonesia":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("IDR"));
        break;
      case "Israel":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("ILS"));
        break;
      case "India":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("INR"));
        break;
      case "Iceland":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("ISK"));
        break;
      case "Japan":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("JPY"));
        break;
      case "Korea":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("KRW"));
        break;
      case "Kazakhstan":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("KZT"));
        break;
      case "Mexico":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("MXN"));
        break;
      case "Malaysia":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("MYR"));
        break;
      case "Norway":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("NOK"));
        break;
      case "New Zealand":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("NZD"));
        break;
      case "Panama":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PAB"));
        break;
      case "Peru":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PEN"));
        break;
      case "Philippines":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PHP"));
        break;
      case "Pakistan":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PKR"));
        break;
      case "Poland":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PLN"));
        break;
      case "Paraguay":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("PYG"));
        break;
      case "Romania":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("RON"));
        break;
      case "Russia":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("RUB"));
        break;
      case "Saudi Arabia":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("SAR"));
        break;
      case "Sweden":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("SEK"));
        break;
      case "Sudan":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("SGD"));
        break;
      case "Thailand":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("THB"));
        break;
      case "Turkey":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("TRY"));
        break;
      case "Taiwan":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("TWD"));
        break;
      case "Ukraine":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("UAH"));
        break;
      case "Uruguay":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("UYU"));
        break;
      case "Viet Nam":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("VND"));
        break;
      case "South Africa":
        exchangeRate = BigDecimal.valueOf(rates.getDouble("ZAR"));
        break;
    }

    return exchangeRate;
  }

}
