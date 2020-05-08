const mongoose = require("mongoose");
const { MongoMemoryServer } = require("mongodb-memory-server");

let mongod, connection;

const getConnection = () => {
    if (!connection) {
        throw new Error("monogodb connection hasn't been created yet");
    }

    return connection;
};

const start = async () => {
    if (!mongod) {
        mongod = new MongoMemoryServer();

        const uri = await mongod.getUri();
        connection = await mongoose.createConnection(uri);
    }
};

const stop = async () => {
    if (connection) {
        await connection.close();
    }

    if (mongod) {
        await mongod.stop();
    }
};

module.exports = {
    start,
    stop,
    getConnection,
};
