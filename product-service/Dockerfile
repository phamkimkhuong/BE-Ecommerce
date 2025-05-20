# Khai báo ARG trước khi sử dụng trong FROM
ARG DOCKERHUB_USERNAME

# Sử dụng base-builder từ base-builder.Dockerfile
FROM ${DOCKERHUB_USERNAME}/base-builder:latest AS product-builder

# Copy và build product-service
COPY ./product-service/pom.xml ./product-service/
COPY ./product-service/src ./product-service/src
RUN cd product-service && mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=product-builder /app/product-service/target/*.jar ./product-service.jar

## Set environment variables explicitly to ensure proper config server connection
ENV SPRING_PROFILES_ACTIVE=docker
ENV SPRING_CLOUD_CONFIG_URI=http://config-server:8888
#ENV SPRING_CONFIG_IMPORT=optional:configserver:http://config-server:8888

EXPOSE 9898
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "product-service.jar"]