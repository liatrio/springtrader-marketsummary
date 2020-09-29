const autoIncrementFactory = require("mongoose-sequence");

const repository = require("./index");
const quoteSchema = require("./schema/quote");

let setup = false;

const Quote = () => {
    const connection = repository.getConnection();

    if (!setup) {
        const autoIncrement = autoIncrementFactory(connection);

        quoteSchema.plugin(autoIncrement, {
            inc_field: "quoteid",
        });

        setup = true;
    }

    return connection.model("Quote", quoteSchema);
};

module.exports = {
    Quote,
};
