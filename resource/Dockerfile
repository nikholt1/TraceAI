FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /app
COPY . .

ARG MODULE=resource

RUN mvn -B -pl ${MODULE} -am dependency:go-offline
RUN mvn -B clean package -DskipTests -pl ${MODULE} -am


FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

ARG MODULE=resource
COPY --from=build /app/${MODULE}/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]