const mongoose = require("mongoose");

let connection;

const getConnection = () => {
    if (!connection) {
        throw new Error("monogodb connection hasn't been created yet");
    }

    return connection;
};

const start = async () => {
    if (!connection) {
        connection = await mongoose.createConnection(`mongodb://mongodb.${process.env.DATABASE_NAMESPACe}.svc.cluster.local:27017`);
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
