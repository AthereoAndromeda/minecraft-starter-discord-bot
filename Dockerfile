# Use the OpenJDK 18 image as the base image for the build stage
FROM eclipse-temurin:18-jdk AS build

COPY . /app
WORKDIR /app

RUN ./gradlew shadowJar

# Use the OpenJDK 18 JRE alpine image as the base image for the runtime stage
FROM eclipse-temurin:18-jre-alpine

# Copy the JAR file from the build stage into the container
COPY --from=build /app/app/build/libs/app-all.jar /app/app-all.jar

# Set the working directory to /app
WORKDIR /app

ENV DISCORD_TOKEN=example
ENV NGROK_WEB_URL=http://localhost:4040/api/tunnels/minecraft

# Run the JAR file when the container starts
CMD ["java", "-jar", "app-all.jar"]

