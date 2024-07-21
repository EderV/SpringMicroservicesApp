FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-scheduler-${APP_VERSION}.jar /ms-scheduler-${APP_VERSION}.jar

ENTRYPOINT ["java","-jar","/ms-scheduler-${APP_VERSION}.jar"]