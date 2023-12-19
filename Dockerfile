# Start with a base image that includes Java and Maven
FROM maven

# Set the working directory in the Docker image
WORKDIR /app

# Copy the Maven POM file and source code into the image
COPY pom.xml .
COPY src /app/src/

# Build the application
RUN mvn clean package

# Set the image to start from a base Java image
FROM openjdk

# Set the working directory in the image
WORKDIR /app

# Copy the JAR file from the previous stage
COPY --from=0 /app/target/TjackServer-1.0.0-jar-with-dependencies.jar /app/app.jar

EXPOSE 4231
# Command to run the application
CMD ["java", "-jar", "/app/app.jar"]
