# Sử dụng base-builder từ base-builder.Dockerfile
FROM base-builder AS payment-builder

# Copy và build payment-service
COPY ./payment-service/pom.xml ./payment-service/
COPY ./payment-service/src ./payment-service/src
RUN cd payment-service && mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=payment-builder /app/payment-service/target/*.jar ./payment-service.jar

EXPOSE 9997
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "payment-service.jar"]