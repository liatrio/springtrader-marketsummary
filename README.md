# springtrader-marketsummary

## Overview

This repository is for the marketsummary microservice that was taken from the 
springtrader monolith application found in the [liatrio/springtrader](https://github.com/liatrio/springtrader)
repository. 

This microservice is essentially just a standalone springboot application with only
the MarketSummary controller java class and the dependencies it needs to run along with 
an H2 database. There is only one endpoint located at the path `/marketsummary`. This project
uses `gradle 5.x` to build and `java 8` to run.

Note: The original monolith uses `gradle 1.2` to build and `java 7` to run

## Running locally

If you have docker installed you can build and run the container locally via
```
docker build -t marketsummary .
docker run -p 8080:8080 marketsummary
```
To check if it's running you can either visit `http://localhost:8080/marketsummary` in Google Chrome
or you can run `curl localhost:8080/marketsummary` from the commandline. If the build was 
successful you should see receive some JSON data of stock prices.

## Running via Lead SDM

As long as there is an instance of the monolith and the microservice running on the same product namespace 
istio should be routing web traffic that attempts to hit the `/marketsummary` endpoint to the microservice.
