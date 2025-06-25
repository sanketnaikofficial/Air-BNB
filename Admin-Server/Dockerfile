FROM openjdk:17

COPY target/adminserver.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 5555

ENTRYPOINT ["java", "-jar", "adminserver.jar"]