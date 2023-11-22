FROM openjdk:19
LABEL authors="Vadim Taratonov"
EXPOSE 7777
ARG JAR_FILE=/target/gateway-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} gateway

CMD ["java","-jar","gateway"]