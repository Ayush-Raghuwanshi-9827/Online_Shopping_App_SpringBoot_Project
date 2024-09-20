FROM openjdk:17-jdk-alpine
COPY target/Online_Shopping_App-0.0.1-SNAPSHOT.jar /app/Online_Shopping_App.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "Online_Shopping_App.jar"]
