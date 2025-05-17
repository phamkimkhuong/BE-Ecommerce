# Sử dụng base-builder từ base-builder.Dockerfile
FROM base-builder AS order-builder

# Copy và build order-service
COPY ./order-service/pom.xml ./order-service/
COPY ./order-service/src ./order-service/src
RUN cd order-service && mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=order-builder /app/order-service/target/*.jar ./order-service.jar

EXPOSE 9292
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "order-service.jar"]