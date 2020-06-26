const hapi = require("@hapi/hapi");
const mongoose = require("mongoose");
const { loadQuoteData } = require("./src/repository/data");


const repository = require("./src/repository");
const marketSummaryController = require("./src/controller/marketsummary");
const { addTracing, closeTracer } = require("./src/util/tracing");

(async () => {
    const server = hapi.server({
        port: 5555,
        host: "0.0.0.0",
    });

    server.route({
        method: "GET",
        path: "/healthz",
        handler: () => "ok",
    });

    await repository.start();
    
    /*
    mongoose.connection.db.listCollections({name: 'quotes'})
        .next(function(err, exists) {
            if (!exists) {
                async() => {
                    await loadQuoteData();
                };
            }
        });
    */

    marketSummaryController(server);
    addTracing(server);

    await server.start();

    console.log("Server running on %s", server.info.uri);

    ["SIGINT", "SIGTERM"].forEach((signal) => {
        process.on(signal, async () => {
            console.log("Termination signal %s received, stopping...", signal);

            await stop(server);
        });
    });

    process.on("unhandledRejection", async (err) => {
        console.log("Unhandled promise rejection", err);

        await stop(server, 1);
    });
})();

const stop = async (server, code = 0) => {
    try {
        await server.stop();

        await closeTracer();
        await repository.stop();
    } catch (e) {
        console.log("Error stopping server", e);

        return process.exit(1);
    }

    return process.exit(code);
};
