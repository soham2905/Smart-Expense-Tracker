# ---- Stage 1: Build the app with Maven ----
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy everything (pom.xml, mvnw, src/) and build the jar
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# ---- Stage 2: Run the app with just a JRE (smaller, faster image) ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy only the built jar from the build stage above
COPY --from=build /app/target/expense-tracker.jar app.jar

# Render assigns the actual port via the PORT env variable at runtime;
# application.properties already reads it via server.port=${PORT:8080}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
