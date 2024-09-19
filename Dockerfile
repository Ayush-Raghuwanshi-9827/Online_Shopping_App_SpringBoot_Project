# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Create a directory for the H2 database
RUN mkdir -p /app/h2db

# Add the volume for the H2 database
VOLUME ["/app/h2db"]

# Package the application and place it in /app
RUN ./mvnw clean package -DskipTests

# Run the jar file
CMD ["java", "-jar", "target/Online_Shopping_App-0.0.1-SNAPSHOT.jar"]
