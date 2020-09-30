const fs = require("fs");
const util = require("util");
const mongoose = require("mongoose");
const config = require("config");
const watch = require("node-watch");

const CREDENTIALS_FILE = config.get("database.credentialsFile");

let connection, watcher;

async function getDatabaseCredentials() {
    const data = await util.promisify(fs.readFile)(CREDENTIALS_FILE);

    return JSON.parse(data);
}

const getConnection = () => {
    if (!connection) {
        throw new Error("monogodb connection hasn't been created yet");
    }

    return connection;
};

async function createConnection(username, password) {
    const hostname = config.get("database.hostname"),
        port = config.get("database.port"),
        databaseName = config.get("database.databaseName");
    const connectionString = `mongodb://${username}:${password}@${hostname}:${port}/${databaseName}?authSource=admin`
    console.log('Using connection string:', connectionString)
    const newConnection = await mongoose.createConnection(connectionString);
    return newConnection
}

const start = async () => {
    const { username, password } = await getDatabaseCredentials();

    connection = await createConnection(username, password);

    watcher = watch(CREDENTIALS_FILE, async (event) => {
        console.log("received watch event:", event);

        const { username, password } = await getDatabaseCredentials();

        connection = await createConnection(username, password);
    });
};

const stop = async () => {
    if (watcher) {
        watcher.close();
    }

    if (connection) {
        await connection.close();
    }
};

module.exports = {
    start,
    stop,
    getConnection,
};
