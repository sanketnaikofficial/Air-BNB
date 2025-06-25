FROM openjdk:17

COPY target/serviceregistry.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "serviceregistry.jar"]