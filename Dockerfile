# Start with a base image containing Java runtime
FROM arm64v8/eclipse-temurin:17-jdk AS build

# Image maintainer
LABEL maintainer=ericbouchut.com
LABEL authors="eric"

# Add the application's jar to the container
COPY target/safetynet-0.0.1-SNAPSHOT.jar safetynet-0.0.1-SNAPSHOT.jar

# Document the port the application listens on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar" ,"/safetynet-0.0.1-SNAPSHOT.jar"]