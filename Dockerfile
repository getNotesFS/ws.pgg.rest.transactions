# Stage 1: Build stage
FROM maven:3.8.5-openjdk-17-slim as build-stage
WORKDIR /app
# Copy your source files
COPY ./src ./src
COPY pom.xml .
# Build your project and package it into a WAR file
RUN mvn clean package -DskipTests

# Stage 2: Run stage
# Use Tomcat with JDK 17 for the runtime environment
FROM tomcat:jre17-temurin-jammy
WORKDIR /usr/local/tomcat

# Optionally copy server configuration
#COPY --from=build-stage /path/to/your/server.xml ./conf/

# Copy the war file from the build stage to the Tomcat webapps directory
COPY --from=build-stage /app/target/transactions-0.0.1.war ./webapps/ws.pgg.rest.transactions.war

# Copy any other necessary files like data directories directly from your project
# Ensure these are necessary for your application to run
COPY ./data /usr/local/tomcat/data

# Expose the port Tomcat listens on
EXPOSE 8080
