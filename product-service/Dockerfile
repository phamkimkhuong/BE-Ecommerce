FROM amazoncorretto:21.0.4

WORKDIR /app

COPY --from=all-services:latest /app/services/product-service.jar ./app.jar

EXPOSE 9898
ENTRYPOINT ["java","-jar","app.jar"]