FROM openjdk:17

COPY target/api-gateway.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 5556

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]