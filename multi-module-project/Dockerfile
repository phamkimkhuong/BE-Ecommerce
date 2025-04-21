FROM maven:3.9.8-amazoncorretto-21 AS builder

WORKDIR /app

# Copy Maven repo từ volume (nếu có)
COPY --from=common-service:1.0.0 /maven-repo /root/.m2/repository

# Copy parent POM vào thư mục gốc
COPY ./multi-module-project/pom.xml pom.xml

# Tạo thư mục cho product-service
RUN mkdir -p product-service

# Copy module product-service
COPY ./product-service ./product-service

# Install parent POM trước
RUN mvn install -N -DskipTests

# Install toàn bộ project
RUN mvn install -DskipTests

FROM amazoncorretto:21.0.4
COPY --from=builder /root/.m2/repository /maven-repo
VOLUME /maven-repo