# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal runtime environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

COPY --from=builder /app/target/resilience-gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7860

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-XX:+UseG1GC", "-jar", "app.jar"]
