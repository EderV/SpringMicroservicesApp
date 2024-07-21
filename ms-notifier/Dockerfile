FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-notifier-${APP_VERSION}.jar /ms-notifier-${APP_VERSION}.jar

EXPOSE 8094

ENTRYPOINT ["java","-jar","/ms-notifier-${APP_VERSION}.jar"]