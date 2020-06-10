const { getMarketSummary } = require("../service/marketsummary-service");
const { convertCurrency } = require("../service/currency-service");
const { getLocaleFromRequest } = require("../util/helpers");

const marketsummary = (server) => {
    server.route({
        method: "GET",
        path: "/marketsummary",
        handler: async (request, h) => {
            const marketsummary = await getMarketSummary();
            const locale = getLocaleFromRequest(request);

            return convertCurrency(marketsummary, locale);
        },
    });
};

module.exports = marketsummary;
