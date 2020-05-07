# springtrader-marketsummary

## Overview

This repository is for the marketsummary microservice that was taken from the 
springtrader monolith application found in the [liatrio/springtrader](https://github.com/liatrio/springtrader)
repository. 

TODO: brief description

## Running locally

TODO

## Running via Lead SDM

As long as there is an instance of the monolith and the microservice running on the same product namespace 
istio should be routing web traffic that attempts to hit the `/marketsummary` endpoint to the microservice.
