const { Quote } = require("../repository/models");

const marketsummary = (server) => {
    server.route({
        method: "GET",
        path: "/marketsummary",
        handler: async (request, h) => {
            const quotes = await Quote().find({}).lean();

            return quotes;
        },
    });
};

module.exports = marketsummary;
