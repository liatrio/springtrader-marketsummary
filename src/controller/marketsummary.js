const { getMarketSummary } = require("../service/marketsummary");

const marketsummary = (server) => {
    server.route({
        method: "GET",
        path: "/marketsummary",
        handler: (request, h) => {
            return getMarketSummary();
        },
    });
};

module.exports = marketsummary;
