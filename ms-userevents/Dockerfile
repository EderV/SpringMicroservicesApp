FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-userevents-${APP_VERSION}.jar /ms-userevents-${APP_VERSION}.jar

ENTRYPOINT ["java","-jar","/ms-userevents-${APP_VERSION}.jar"]