# Multi-Module Project cho Web Ecommerce

Đây là project chứa các dependency dùng chung cho tất cả các microservice.

## Cách sử dụng trên Docker

Multi-module-project được đóng gói thành một image Docker chứa Maven repository với tất cả các dependency đã được cài đặt. Các service khác có thể sử dụng repository này trong quá trình build.

### 1. Build multi-module-project

```bash
docker-compose build multi-module-project
```

### 2. Cách sử dụng trong Dockerfile của các service

Trong Dockerfile của mỗi service, bạn có thể sử dụng multi-module-project như sau:

```dockerfile
FROM maven:3.9-eclipse-temurin-21-alpine as builder

WORKDIR /app

# Sao chép settings.xml để sử dụng local repository
COPY ./docker-maven-settings.xml /root/.m2/settings.xml

# Sao chép pom.xml và mã nguồn
COPY pom.xml .
COPY src ./src

# Sử dụng Maven repository từ multi-module-project
COPY --from=multi-module-repo:1.0.0 /maven-repo /root/.m2/repository

# Build ứng dụng
RUN mvn package -DskipTests

# Stage 2: Tạo application image
FROM eclipse-temurin:21-alpine

WORKDIR /app

# Sao chép file JAR từ builder stage
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3. Tạo file docker-maven-settings.xml

Tạo file `docker-maven-settings.xml` trong thư mục gốc của dự án:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository>/root/.m2/repository</localRepository>
  <interactiveMode>false</interactiveMode>
  <offline>true</offline>
</settings>
```

### 4. Cập nhật docker-compose.yml

Đảm bảo các service khác phụ thuộc vào multi-module-project bằng cách thêm:

```yaml
depends_on:
  - multi-module-project
```

## Lưu ý

- Khi thay đổi multi-module-project, cần rebuild lại image của nó trước khi build các service khác.
- Đối với môi trường phát triển, hãy sử dụng `mvn install` để cài đặt vào local repository trên máy của bạn. 