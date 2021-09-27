FROM gradle:5.5-jdk8 as builder

WORKDIR /app
ENV GRADLE_OPTS -Dorg.gradle.daemon=false

# Keep gradle dependencies in separate image layer
COPY build.gradle settings.gradle ./
RUN gradle classes || echo "ok"

# Copy in the src and build
COPY src /app/src
RUN gradle build

FROM openjdk:8-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/springtrader-marketSummary.jar .
ENTRYPOINT ["java","-jar","springtrader-marketSummary.jar"]
EXPOSE 8080
