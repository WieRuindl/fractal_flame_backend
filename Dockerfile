# Use an official Gradle image as the base image for building
FROM gradle:7.4.2-jdk17 AS build

# Set the working directory
WORKDIR /app

# Copy only the Gradle wrapper and settings files to cache dependencies
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew build

# Step 5: Use an OpenJDK runtime image to run the packaged application
FROM openjdk:17-alpine

# Step 6: Set the working directory in the runtime container
WORKDIR /app

# Step 7: Copy the packaged JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Step 8: Expose the application port
EXPOSE 8080

# Step 9: Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
