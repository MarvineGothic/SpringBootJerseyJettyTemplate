FROM openjdk:21
# Set working directory inside the container
VOLUME /tmp
VOLUME /logs

WORKDIR /app
# Copy the compiled Java application JAR file into the container
COPY ./target/spring-boot-jersey-clean-architecture.jar /app
# Expose the port the Spring Boot application will run on
EXPOSE 8080
# Command to run the application
CMD ["java", "-jar", "spring-boot-jersey-clean-architecture.jar"]