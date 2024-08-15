FROM maven:3.9.7-eclipse-temurin-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17-jdk-alpine
LABEL maintainer="MykhailoTiutiun <mykhailotiutiun@gmail.com>"
COPY --from=build /home/app/target/RepCounterBot.jar RepCounterBot.jar
ENTRYPOINT ["java", "-jar", "/RepCounterBot.jar"]