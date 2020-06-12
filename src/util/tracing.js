const {
    initTracer,
    opentracing,
    ZipkinB3TextMapCodec,
} = require("jaeger-client");
const { SAMPLER_TYPE_CONST } = require("jaeger-client/dist/src/constants");
const boom = require("@hapi/boom");
const { promisify } = require("util");
const config = require("config");
const { getLocaleFromRequest } = require("./helpers");

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
            serviceName:
                process.env.OPENTRACING_JAEGER_SERVICE_NAME || "marketsummary",
            sampler: {
                type: SAMPLER_TYPE_CONST,
                param: 1,
            },
            reporter: {
                logSpans: true,
                collectorEndpoint: config.get("jaegerEndpoint"),
            },
        },
        {}
    );

    const codec = new ZipkinB3TextMapCodec({ urlEncoding: true });

    tracer.registerInjector(opentracing.FORMAT_HTTP_HEADERS, codec);
    tracer.registerExtractor(opentracing.FORMAT_HTTP_HEADERS, codec);
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
        const requireParentTrace = config.get("requireParentTrace");

        if (!spanName) {
            return h.continue;
        }

        const parent = tracer.extract(
            opentracing.FORMAT_HTTP_HEADERS,
            request.headers
        );

        if (parent.toSpanId() || !requireParentTrace) {
            const span = tracer.startSpan(spanName, {
                childOf: requireParentTrace ? parent : undefined,
                tags: {
                    "http.locale": getLocaleFromRequest(request),
                    "http.user-agent": request.headers["user-agent"],
                    "http.host": request.headers.host,
                    "http.path": path,
                    "http.method": method,
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
                if (boom.isBoom(request.response, 500)) {
                    span.log({
                        error: request.response.stack,
                        response: request.response.output,
                    });
                    span.setTag("error", true);
                }

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
