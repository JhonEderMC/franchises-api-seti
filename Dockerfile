FROM gradle:8.14-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew :applications:app-service:build -x test --stacktrace --info

# Run stage
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
VOLUME /tmp
EXPOSE 8080
COPY --from=build /app/applications/app-service/build/libs/*.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
