# Use an OpenJDK 17 Alpine base image
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /usermanager

# Copy the Maven wrapper and pom.xml to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline

# Copy the source code into the container
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

ARG JAR_FILE=target/usermanager*.jar
COPY ${JAR_FILE} usermanager.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
