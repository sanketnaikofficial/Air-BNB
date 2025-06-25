FROM openjdk:17

COPY target/configserver.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "configserver.jar"]