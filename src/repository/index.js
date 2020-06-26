const mongoose = require("mongoose");
const { loadQuoteData } = require("./src/repository/data");


let connection;

const getConnection = () => {
    if (!connection) {
        throw new Error("monogodb connection hasn't been created yet");
    }

    return connection;
};

const start = async () => {
    if (!connection) {
        console.log("Connecting to DB");
        connection = await mongoose.createConnection(`mongodb://mongodb.${process.env.DATABASE_NAMESPACE}.svc.cluster.local:27017/${process.env.NODE_ENV}`);
        connection.db.listCollections().toArray(function (err, collectionNames) {
            if (err) {
                console.log(err);
                loadQuoteData();
            }
            console.log(collectionNames);
        })
    }
};

const stop = async () => {
    if (connection) {
        await connection.close();
    }
};

module.exports = {
    start,
    stop,
    getConnection,
};
