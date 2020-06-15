const { Quote } = require("../repository/models");

const getMarketSummary = async () => {
    const losers = await Quote().find(
        {},
        { _id: 0 },
        { sort: { change1: 1 }, limit: 3 }
    );

    const winners = await Quote().find(
        {},
        { _id: 0 },
        { sort: { change1: -1 }, limit: 3 }
    );

    const summary = await Quote().aggregate([
        {
            $facet: {
                tradeStockIndexAverage: [
                    { $group: { _id: null, value: { $avg: "$price" } } },
                    { $project: { _id: 0, value: { $round: ["$value", 2] } } },
                ],
                tradeStockIndexOpenAverage: [
                    { $group: { _id: null, value: { $avg: "$open1" } } },
                    { $project: { _id: 0, value: { $round: ["$value", 2] } } },
                ],
                tradeStockIndexVolume: [
                    { $group: { _id: null, value: { $sum: "$volume" } } },
                    { $project: { _id: 0, value: 1 } },
                ],
                change: [
                    { $group: { _id: null, value: { $sum: "$change1" } } },
                    { $project: { _id: 0, value: 1 } },
                ],
            },
        },
    ]);

    const {
        tradeStockIndexAverage,
        tradeStockIndexOpenAverage,
        tradeStockIndexVolume,
        change,
    } = summary[0];

    return {
        tradeStockIndexAverage: tradeStockIndexAverage[0].value,
        tradeStockIndexVolume: tradeStockIndexVolume[0].value,
        tradeStockIndexOpenAverage: tradeStockIndexOpenAverage[0].value,
        topLosers: losers,
        topGainers: winners,
        summaryDate: new Date().toISOString(),
        change: change[0].value,
        percentGain: 0,
    };
};

module.exports = {
    getMarketSummary,
};
