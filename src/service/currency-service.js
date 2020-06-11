const fetch = require("node-fetch");
const LocaleCurrency = require("locale-currency");
const BigNumber = require("bignumber.js");

let rates;

const loadCurrencyConversionRates = async () => {
    const response = await fetch(
        "https://api.exchangerate-api.com/v4/latest/USD"
    );

    const currencyRates = (await response.json()).rates;

    rates = Object.keys(currencyRates).reduce(
        (acc, key) => ({
            ...acc,
            [key]: {
                rate: currencyRates[key],
            },
        }),
        {}
    );
};

const getCurrencyConversionRateFromLocale = async (locale) => {
    if (!rates) {
        await loadCurrencyConversionRates();
    }

    const currencyCode = LocaleCurrency.getCurrency(locale);

    return rates[currencyCode].rate;
};

const convertCurrency = async (marketSummary, locale) => {
    const currencyConversionRate = await getCurrencyConversionRateFromLocale(
        locale
    );

    const tradeStockIndexAverage = new BigNumber(
        marketSummary.tradeStockIndexAverage
    );
    const tradeStockIndexOpenAverage = new BigNumber(
        marketSummary.tradeStockIndexOpenAverage
    );

    const bigNumberExchangeRate = new BigNumber(currencyConversionRate);

    return {
        ...marketSummary,
        tradeStockIndexAverage: tradeStockIndexAverage
            .times(bigNumberExchangeRate)
            .decimalPlaces(2)
            .toNumber(),
        tradeStockIndexOpenAverage: tradeStockIndexOpenAverage
            .times(bigNumberExchangeRate)
            .decimalPlaces(2)
            .toNumber(),
    };
};

module.exports = {
    convertCurrency,
};
