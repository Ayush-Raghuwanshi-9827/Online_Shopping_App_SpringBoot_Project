# Use a base image with Java runtime
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/Online_Shopping_App-0.0.1-SNAPSHOT.jar /app/Online_Shopping_App.jar

# Set environment variables for the database connection
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql.railway.internal:3306/railway
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=ChIfVQCfhTJvseMqXJnqcNtNQsDqeTxj

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/Online_Shopping_App.jar"]
