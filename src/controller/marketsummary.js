const { getMarketSummary } = require("../service/marketsummary-service");

const marketsummary = (server) => {
    server.route({
        method: "GET",
        path: "/marketsummary",
        handler: async (request, h) => {
            const marketsummary = await getMarketSummary();

            return marketsummary;
        },
    });
};

module.exports = marketsummary;
