FROM openjdk:17

COPY target/bookingservice.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "bookingservice.jar"]