FROM openjdk:8
ADD target/socket-service.jar socket-service.jar
EXPOSE 9100:9100
ENTRYPOINT ["java", "-jar","socket-service.jar"]

