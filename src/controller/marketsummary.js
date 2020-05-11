const { getMarketSummary } = require("../service/marketsummary");

const marketsummary = (server) => {
    server.route({
        method: "GET",
        path: "/marketsummary",
        handler: async (request, h) => getMarketSummary(),
    });
};

module.exports = marketsummary;
