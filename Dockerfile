FROM maven:3.9.8-amazoncorretto-21 AS builder

WORKDIR /app

# 1. Build common-service (thư viện chung)
COPY ./common-service/pom.xml ./common-service/
COPY ./common-service/src ./common-service/src
RUN cd common-service && mvn clean install -DskipTests

# 2. Build multi-module-project (parent project)
COPY ./multi-module-project/pom.xml ./multi-module-project/
RUN cd multi-module-project && mvn install -N -DskipTests

# 3. Build tất cả các service con
COPY ./product-service ./product-service
#COPY ./user-service ./user-service

# Build từng service
RUN cd product-service && mvn clean package -DskipTests
# Thêm các lệnh build cho service khác

# Tạo thư mục để chứa tất cả các JAR files
RUN mkdir -p /app/services
RUN cp ./product-service/target/*.jar /app/services/product-service.jar
# Thêm các service khác nếu cần