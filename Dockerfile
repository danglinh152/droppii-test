# Build stage
FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY . .
# Chỉ chạy lệnh install nếu cần thiết
RUN mvn install -DskipTests

# Runtime stage
FROM eclipse-temurin:23-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar demo.jar
# Expose port
EXPOSE 8080
# Entry point
ENTRYPOINT ["java", "-jar", "demo.jar"]
