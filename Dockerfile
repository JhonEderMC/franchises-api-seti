# Build stage
FROM gradle:8.14-jdk21 AS build
WORKDIR /app

# Copy build files
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ ./gradle/

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY . .

# Build the application
RUN ./gradlew bootJar --no-daemon --stacktrace --info

# Final stage
FROM eclipse-temurin:21-jre-jammy

# Set environment variables
ENV TZ=America/Lima \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod"

# Create a non-root user
RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --gid 1001 --shell /bin/false appuser

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY --from=build /app/build/libs/franquicias-api.jar /app/app.jar

# Set file permissions
RUN chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
