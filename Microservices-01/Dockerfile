FROM openjdk:17

COPY target/microservices01.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 9091

ENTRYPOINT ["java", "-jar", "microservices01.jar"]