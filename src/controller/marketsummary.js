const { getMarketSummary } = require("../service/marketsummary");

const marketsummary = (server) => {
    server.route({
        method: "GET",
        path: "/marketsummary",
        handler: (request, h) => {
            // console.log("controller headers", request.headers);

            return getMarketSummary();
        },
    });
};

module.exports = marketsummary;
