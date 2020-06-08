const hapi = require("@hapi/hapi");
const { initTracer, opentracing } = require("jaeger-client");
const { promisify } = require("util");

const repository = require("./src/repository");
const { loadQuoteData } = require("./src/repository/data");
const marketSummaryController = require("./src/controller/marketsummary");

const tracer = initTracer(
    {
        serviceName: "marketSummary",
        sampler: {
            type: "const",
            param: 1,
        },
        reporter: {
            logSpans: true, // this logs whenever we send a span
            collectorEndpoint: "http://localhost:14268/api/traces",
        },
    },
    {
        logger: {
            info: function (msg) {
                console.log("INFO  ", msg);
            },
            error: function (msg) {
                console.log("ERROR ", msg);
            },
        },
    }
);

const closeTracer = promisify(tracer.close);

const spans = {};

(async () => {
    const server = hapi.server({
        port: 5555,
        host: "0.0.0.0",
    });

    server.route({
        method: "GET",
        path: "/",
        handler: (request, h) => {
            return "hello world";
        },
    });

    server.route({
        method: "GET",
        path: "/healthz",
        handler: (request, h) => {
            return "ok";
        },
    });

    await repository.start();
    await loadQuoteData();

    marketSummaryController(server);

    server.ext("onPreHandler", (request, h) => {
        // console.log("onPreHandler headers", request.headers);

        console.log("spans", spans);

        const parent = tracer.extract(
            opentracing.FORMAT_HTTP_HEADERS,
            request.headers
        );

        // if (parent.toSpanId()) {
        const span = tracer.startSpan("findMarketSummary", {
            // childOf: parent,
            tags: {
                "http.locale": request.headers["accept-language"],
            },
        });

        tracer.inject(span, opentracing.FORMAT_HTTP_HEADERS, request.headers);
        // }

        spans[span.context().toSpanId()] = span;

        return h.continue;
    });

    server.ext("onPreResponse", (request, h) => {
        const spanContext = tracer.extract(
            opentracing.FORMAT_HTTP_HEADERS,
            request.headers
        );

        if (spanContext.toSpanId()) {
            const span = spans[spanContext.toSpanId()];

            if (span) {
                span.finish();
                delete spans[spanContext.toSpanId()];
            }
        }

        return h.continue;
    });

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
