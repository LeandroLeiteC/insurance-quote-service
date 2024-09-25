# build stage
FROM gradle:8.9-jdk17-alpine AS builder

WORKDIR /usr/app/

COPY . .

RUN gradle bootJar

# build runtime
FROM eclipse-temurin:17-jre

WORKDIR /opt/app

COPY --from=builder /usr/app/build/libs/application.jar application.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar .

CMD ["java", "-javaagent:opentelemetry-javaagent.jar", "-jar", "application.jar"]