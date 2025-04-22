# Sử dụng base-builder từ base-builder.Dockerfile
FROM base-builder AS product-builder

# Copy và build product-service
COPY ./product-service/pom.xml ./product-service/
COPY ./product-service/src ./product-service/src
RUN cd product-service && mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=product-builder /app/product-service/target/*.jar ./app.jar

## Set environment variables explicitly to ensure proper config server connection
ENV SPRING_PROFILES_ACTIVE=docker
ENV SPRING_CLOUD_CONFIG_URI=http://config-server:8888
#ENV SPRING_CONFIG_IMPORT=optional:configserver:http://config-server:8888
#ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/

EXPOSE 9898
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]