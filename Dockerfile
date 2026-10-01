# ==============================================================================
# Multi-Stage Production Dockerfile for Railway Deployment
# Application: Employee Performance Management System (EPS)
# Stack: Java 17, Jakarta Servlets 6.0, Apache Tomcat 10.1, MySQL 8
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build the Maven WAR package
# ------------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and resolve dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy application source code
COPY src ./src

# Compile and package production WAR
RUN mvn clean package -DskipTests

# ------------------------------------------------------------------------------
# Stage 2: Production Apache Tomcat 10.1 Runtime
# ------------------------------------------------------------------------------
FROM tomcat:10.1-jdk17-temurin

LABEL maintainer="EPS Enterprise"
LABEL description="Employee Performance Evaluation System on Apache Tomcat 10"

WORKDIR /usr/local/tomcat

# Clean default web applications
RUN rm -rf webapps/*

# Deploy WAR as ROOT.war (root context '/') and eps.war (context '/eps/')
COPY --from=builder /app/target/eps.war webapps/ROOT.war
COPY --from=builder /app/target/eps.war webapps/eps.war

# Copy Railway dynamic PORT entrypoint script
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# Default port (Railway dynamically injects PORT)
EXPOSE 8080
ENV PORT=8080

ENTRYPOINT ["/docker-entrypoint.sh"]
