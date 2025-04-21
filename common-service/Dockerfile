FROM maven:3.9.8-amazoncorretto-21 AS builder

WORKDIR /app

# Copy pom.xml và cài đặt dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build và install common-service vào local repository
RUN mvn clean install -DskipTests

# Tạo thư mục để chứa artifact
RUN mkdir -p /maven-repo/com/backend/common-service/1.0.0/
RUN cp target/common-service-1.0.0.jar /maven-repo/com/backend/common-service/1.0.0/
RUN cp pom.xml /maven-repo/com/backend/common-service/1.0.0/common-service-1.0.0.pom

# Stage 2: Final image
FROM amazoncorretto:21.0.4

# Set the working directory
WORKDIR /app

# Copy JAR file
COPY --from=builder /app/target/*.jar common-service.jar
# Copy Maven repository
COPY --from=builder /maven-repo /maven-repo

# Volume để chia sẻ Maven repository
VOLUME /maven-repo