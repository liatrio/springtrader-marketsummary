const autoIncrementFactory = require("mongoose-sequence");

const repository = require("./index");
const quoteSchema = require("./schema/quote");

const Quote = () => {
    const connection = repository.getConnection();
    const autoIncrement = autoIncrementFactory(connection);

    quoteSchema.plugin(autoIncrement, {
        inc_field: "quoteid",
    });

    return connection.model("Quote", quoteSchema);
};

module.exports = {
    Quote,
};
