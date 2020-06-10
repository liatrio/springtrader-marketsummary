const fetch = require("node-fetch");
const LocaleCurrency = require("locale-currency");
const BigNumber = require("bignumber.js");
const boom = require("@hapi/boom");

let rates;

const loadRates = async () => {
    const response = await fetch(
        "https://api.exchangerate-api.com/v4/latest/USD"
    );

    rates = (await response.json()).rates;
};

const getRate = async (currencyCode) => {
    if (!rates) {
        await loadRates();
    }

    return rates[currencyCode];
};

const convertCurrency = async (marketSummary, locale) => {
    const currencyCode = LocaleCurrency.getCurrency(locale);

    if (currencyCode === "USD") {
        return marketSummary;
    }

    if (!currencyCode) {
        throw boom.badImplementation(
            `could not find currency code for locale: ${locale}`
        );
    }

    const tradeStockIndexAverage = new BigNumber(
        marketSummary.tradeStockIndexAverage
    );
    const tradeStockIndexOpenAverage = new BigNumber(
        marketSummary.tradeStockIndexOpenAverage
    );

    const exchangeRate = await getRate(currencyCode);
    const bigNumberExchangeRate = new BigNumber(exchangeRate);

    return {
        ...marketSummary,
        tradeStockIndexAverage: tradeStockIndexAverage
            .times(bigNumberExchangeRate)
            .toFixed(2),
        tradeStockIndexOpenAverage: tradeStockIndexOpenAverage
            .times(bigNumberExchangeRate)
            .toFixed(2),
    };
};

module.exports = {
    convertCurrency,
};
