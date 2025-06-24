FROM gradle:8.14-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon --stacktrace --info

# Run stage
FROM openjdk:21-jdk-slim
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/build/libs/franquicias-api.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
