FROM  maven:3.9.8-amazoncorretto-21 AS builder

WORKDIR /app

# Sao chép file pom.xml vào container
COPY pom.xml .

# Cài đặt project vào local repository của Maven
RUN mvn install -DskipTests

# Stage 2: Tạo image nhỏ hơn chỉ chứa repository
FROM amazoncorretto:21.0.4

# Sao chép Maven local repository từ stage trước
COPY --from=builder /root/.m2/repository /maven-repo

# Volume để các image khác có thể truy cập repository
VOLUME /maven-repo