# springtrader-marketsummary

## Overview

This repository is for the marketsummary microservice that was taken from the 
springtrader monolith application found in the [liatrio/springtrader](https://github.com/liatrio/springtrader)
repository. 

This microservice encapsulates the functionality found within the [MarketSummary controller](https://github.com/liatrio/springtrader/blob/master/spring-nanotrader-services/src/main/java/org/springframework/nanotrader/web/controller/MarketSummaryController.java),
rewritten in JavaScript. This microservice runs as a standalone application using an embedded database.

The rewritten version of this microservice uses the following technologies:
- [Node.JS](https://nodejs.org/) / JavaScript
- [Yarn](https://yarnpkg.com/)
- [Hapi](https://hapi.dev/)
- [MongoDB](https://www.mongodb.com/)

## Running locally

## Skaffold

- `make local`: Runs `skaffold run` on local Kubernetes cluster.
- `make local-delete`: Cleans up local Kubernetes deployment.

## Node/Docker

After cloning the repository, you can use `yarn` to install the needed dependencies, and `yarn start` to start the application on port 5555.

Optionally, you can also run Jaeger locally to see traces created by the microservice:

```bash
docker run -d --name jaeger \
    -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
    -p 5775:5775/udp \
    -p 6831:6831/udp \
    -p 6832:6832/udp \
    -p 5778:5778 \
    -p 16686:16686 \
    -p 14268:14268 \
    -p 9411:9411 \
    jaegertracing/all-in-one:1.8
```

You can visit the Jaeger UI at http://localhost:16686 and search for traces under the `marketsummary` service.

## Running via Lead SDM

As long as there is an instance of the monolith and the microservice running in the same product namespace, 
istio should be routing web traffic that attempts to hit the `/marketsummary` endpoint to the microservice.
