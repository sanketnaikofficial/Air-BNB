FROM openjdk:17

COPY target/propertyservice.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 9093

ENTRYPOINT ["java", "-jar", "propertyservice.jar"]