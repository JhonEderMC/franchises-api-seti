# Build stage
FROM gradle:8.14-jdk21 AS build
WORKDIR /home/gradle

# Copy Gradle files first to leverage Docker cache
COPY --chown=gradle:gradle build.gradle settings.gradle gradlew ./
COPY --chown=gradle:gradle gradle ./gradle

# Download dependencies
RUN ./gradlew dependencies --no-daemon || return 0

# Copy application source
COPY --chown=gradle:gradle . .

# Build the application
RUN ./gradlew :app-service:bootJar --no-daemon --stacktrace --info

# Final stage
FROM eclipse-temurin:21-jre-jammy

# Set environment variables
ENV TZ=America/Lima \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Create a non-root user
RUN groupadd --system --gid 1001 appuser && \
    useradd --system --uid 1001 --gid 1001 --shell /bin/false appuser

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /home/gradle/applications/app-service/build/libs/app-service-*.jar /app/app.jar

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
