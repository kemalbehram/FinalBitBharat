FROM openjdk:8
ADD target/gateway-server.jar gateway-server.jar
EXPOSE 8765:8765
ENTRYPOINT ["java", "-jar","gateway-server.jar"]

