const autoIncrementFactory = require("mongoose-sequence");

const repository = require("./index");
const quoteSchema = require("./schema/quote");

const models = {};

const Quote = () => {
    if (models.quote) {
        return models.quote;
    }

    const connection = repository.getConnection();
    const autoIncrement = autoIncrementFactory(connection);

    quoteSchema.plugin(autoIncrement, {
        inc_field: "quoteid",
    });

    models.quote = connection.model("Quote", quoteSchema);

    return models.quote;
};

module.exports = {
    Quote,
};
