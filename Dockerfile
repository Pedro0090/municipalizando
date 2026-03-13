FROM maven:3.9-eclipse-temurin-21 AS BUILDER
WORKDIR /app
COPY pom.xml .
COPY api ./api
COPY config ./config
COPY core ./core
COPY etl ./etl
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG MODULO
COPY --from=builder /app/${MODULO}/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
