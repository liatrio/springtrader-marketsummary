const { getMarketSummary } = require("../service/marketsummary");
const { convertCurrency } = require("../service/currencyUtils");
const { getLocaleFromRequest } = require("../util/helpers");

const marketsummary = (server) => {
    server.route({
        method: "GET",
        path: "/marketsummary",
        handler: (request, h) => {
            const marketsummary = getMarketSummary();
            const locale = getLocaleFromRequest(request);
            return convertCurrency(marketsummary, locale);
        },
    });
};

module.exports = marketsummary;
