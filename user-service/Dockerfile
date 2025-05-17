# Sử dụng base-builder từ base-builder.Dockerfile
FROM base-builder AS user-builder

# Copy và build user-service
COPY ./user-service/pom.xml ./user-service/
COPY ./user-service/src ./user-service/src
RUN cd user-service && mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=user-builder /app/user-service/target/*.jar ./user-service.jar

EXPOSE 9995
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "user-service.jar"]