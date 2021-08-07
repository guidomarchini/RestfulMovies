# syntax=docker/dockerfile:1
FROM openjdk:8-jdk-slim
COPY ./target/RestfulMovies-0.0.1-SNAPSHOT.war app.war
EXPOSE 8080
CMD [ "java", "-jar", "app.war" ]

