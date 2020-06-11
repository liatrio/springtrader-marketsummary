const { getMarketSummary } = require("../service/marketsummary-service");
const { convertCurrency } = require("../service/currency-service");
const { getLocaleFromRequest, localeIsValid } = require("../util/helpers");
const boom = require("@hapi/boom");

const marketsummary = (server) => {
    server.route({
        method: "GET",
        path: "/marketsummary",
        handler: async (request, h) => {
            const marketsummary = await getMarketSummary();
            const locale = getLocaleFromRequest(request);

            if (localeIsValid(locale)) {
                return convertCurrency(marketsummary, locale);
            }

            return boom.badRequest(`received invalid locale: ${locale}`);
        },
    });
};

module.exports = marketsummary;
