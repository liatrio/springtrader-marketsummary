const { Quote } = require("../models");
const quoteData = require("./quotes.json");

const loadQuoteData = () => Quote().create(quoteData);

module.exports = {
    loadQuoteData,
};
