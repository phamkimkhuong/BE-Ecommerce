# Sử dụng base-builder từ base-builder.Dockerfile
FROM base-builder AS cart-builder

# Copy và build user-service
COPY ./cart-service/pom.xml ./cart-service/
COPY ./cart-service/src ./cart-service/src
RUN cd cart-service && mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=cart-builder /app/cart-service/target/*.jar ./cart-service.jar

EXPOSE 8088
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "cart-service.jar"]