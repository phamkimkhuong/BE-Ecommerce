# Sử dụng base-builder từ base-builder.Dockerfile
FROM base-builder AS notify-builder

# Copy và build notify-service
COPY ./notification-service/pom.xml ./notification-service/
COPY ./notification-service/src ./notification-service/src
RUN cd notification-service && mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=notify-builder /app/notification-service/target/*.jar ./notification-service.jar

EXPOSE 9003
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "notification-service.jar"]