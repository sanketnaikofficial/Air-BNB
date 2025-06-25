FROM openjdk:17

COPY target/authservice.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 9095

ENTRYPOINT ["java", "-jar", "authservice.jar"]