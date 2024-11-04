# Step 1: Use a Maven image to build the application
FROM maven:3.8.3-openjdk-17 AS build

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the source code into the container
COPY pom.xml .
COPY src ./src

# Step 4: Package the Spring Boot application using Maven
RUN mvn clean package -DskipTests

# Step 5: Use an OpenJDK runtime image to run the packaged application
FROM openjdk:17-alpine

# Step 6: Set the working directory in the runtime container
WORKDIR /app

# Step 7: Copy the packaged JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Step 8: Expose the application port
EXPOSE 8080

# Step 9: Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
