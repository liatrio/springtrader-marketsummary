const { initTracer, opentracing } = require("jaeger-client");
const { SAMPLER_TYPE_CONST } = require("jaeger-client/dist/src/constants");
const { promisify } = require("util");

let tracer;

const spans = {};
const routesToTrace = {
    "/marketsummary": {
        get: "findMarketSummary",
    },
};

const createTracer = () => {
    tracer = initTracer(
        {
            serviceName: "marketSummary",
            sampler: {
                type: SAMPLER_TYPE_CONST,
                param: 1,
            },
            reporter: {
                logSpans: true,
                collectorEndpoint:
                    "http://jaeger-collector.istio-system.svc.cluster.local:14268/api/traces",
            },
        },
        {}
    );
};

const closeTracer = () => promisify(tracer.close)();

const getRouteSpanName = (path, method) => {
    if (routesToTrace[path]) {
        return routesToTrace[path][method];
    }
};

const addTracing = (server) => {
    createTracer();

    server.ext("onPreHandler", (request, h) => {
        const { path, method } = request.route;
        const spanName = getRouteSpanName(path, method);

        if (!spanName) {
            return h.continue;
        }

        const parent = tracer.extract(
            opentracing.FORMAT_HTTP_HEADERS,
            request.headers
        );

        if (parent.toSpanId()) {
            const span = tracer.startSpan(spanName, {
                childOf: parent,
                tags: {
                    "http.locale": request.headers["accept-language"],
                },
            });

            tracer.inject(
                span,
                opentracing.FORMAT_HTTP_HEADERS,
                request.headers
            );

            spans[span.context().toSpanId()] = span;
        }

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
};

module.exports = {
    addTracing,
    closeTracer,
};
