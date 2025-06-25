FROM openjdk:17

COPY target/paymentservice.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 9092

ENTRYPOINT ["java", "-jar", "paymentservice.jar"]