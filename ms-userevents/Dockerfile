FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-userevents-${APP_VERSION}.jar /ms-userevents-${APP_VERSION}.jar

EXPOSE 8090

ENTRYPOINT ["java","-jar","/ms-userevents-${APP_VERSION}.jar"]