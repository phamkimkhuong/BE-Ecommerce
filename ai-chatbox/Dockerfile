# Stage 1: Build the application
#  Start use maven image with jdk 21
FROM maven:3.9.8-amazoncorretto-21 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file into the container
COPY pom.xml .
COPY src ./src

# Build all the dependencies to go offline.
#This is a separate step so the dependencies are cached and only rebuilt when the pom.xml file has changed.
RUN mvn clean package -DskipTests

# Stage 2: Build with amazoncorretto jdk 21
FROM amazoncorretto:21.0.4

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/*.jar chat-box.jar

# Expose the port that the application will run on
EXPOSE 8345

# Run the jar file
ENTRYPOINT ["java", "-jar", "chat-box.jar"]


