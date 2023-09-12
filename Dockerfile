FROM openjdk:17-ea-jdk-slim

VOLUME /tmp

COPY target/api_gateway-1.0.jar ApiGateway.jar

ENTRYPOINT ["java" , "-jar" , "ApiGateway.jar" ]