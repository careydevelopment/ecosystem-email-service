FROM adoptopenjdk/openjdk11:jre-11.0.10_9-alpine
COPY ./target/ecosystem-email-service.jar /
EXPOSE 32050
ENTRYPOINT ["java", "-jar", "./ecosystem-email-service.jar"]
