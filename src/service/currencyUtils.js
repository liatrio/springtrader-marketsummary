const fetch = require("node-fetch");
const LocaleCurrency = require("locale-currency");
const BigNumber = require("bignumber.js");
const boom = require("@hapi/boom");

let rates;

const loadRates = async () => {
    const response = await fetch(
        "https://api.exchangerate-api.com/v4/latest/USD"
    );
    const currentRates = await response.json();
    rates = currentRates;
};

const getRate = async (currencyCode) => {
    if (!rates) {
        await loadRates();
    }

    return rates[currencyCode];
};

const convertCurrency = async (marketSummary, locale) => {
    const tradeStockIndexAverage = new BigNumber(
        marketSummary.tradeStockIndexAverage
    );
    const tradeStockIndexOpenAverage = new BigNumber(
        marketSummary.tradeStockIndexOpenAverage
    );

    const currencyCode = LocaleCurrency.getCurrency(locale);

    if (!currencyCode) {
        throw boom.badImplementation(
            `could not find currency code for locale: ${locale}`
        );
    }

    const exchangeRate = await getRate(currencyCode);
    const bigNumberExchangeRate = new BigNumber(exchangeRate);

    marketSummary.tradeStockIndexAverage = tradeStockIndexAverage
        .times(exchangeRate)
        .toNumber();
    marketSummary.tradeStockIndexOpenAverage = tradeStockIndexOpenAverage
        .times(exchangeRate)
        .toNumber();

    return marketSummary;
};

module.exports = {
    convertCurrency,
};
