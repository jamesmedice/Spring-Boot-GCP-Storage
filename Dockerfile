FROM maven:3.5.2-jdk-8-alpine as BUILD
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:8-jre-alpine
COPY --from=BUILD /app/target/*.jar  application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev,datastore", "/application.jar"]