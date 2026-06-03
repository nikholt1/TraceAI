FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /app


COPY . .


RUN mvn -B -pl bff -am dependency:go-offline


RUN mvn -B clean package -DskipTests -pl bff -am



FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=build /app/bff/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]