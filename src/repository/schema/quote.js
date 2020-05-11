const mongoose = require("mongoose");

module.exports = new mongoose.Schema(
    {
        low: Number,
        open1: Number,
        volume: Number,
        price: Number,
        high: Number,
        companyname: String,
        symbol: String,
        change1: Number,
    },
    {
        versionKey: false,
    }
);
