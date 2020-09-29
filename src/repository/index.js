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

async function setConnection(username, password) {
    const newConnection = await createConnection(username, password);

    connection = newConnection;
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

    connection = await mongoose.createConnection(
        `mongodb://${username}:${password}@${hostname}:${port}/${databaseName}`
    );
}

const start = async () => {
    const { username, password } = await getDatabaseCredentials();

    await createConnection(username, password);

    watcher = watch(CREDENTIALS_FILE, async (event, filename) => {
        console.log(`received watch event on ${filename}: ${event}`);

        const { username, password } = await getDatabaseCredentials();

        await setConnection(username, password);
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
