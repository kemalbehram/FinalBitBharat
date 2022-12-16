FROM openjdk:8-jre-alpine
ADD target/wallet-service.jar wallet-service.jar
EXPOSE 8100:8100
ENTRYPOINT ["java", "-jar","wallet-service.jar"]

