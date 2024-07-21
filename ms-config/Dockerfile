FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-config-${APP_VERSION}.jar /ms-auth-${APP_VERSION}.jar

EXPOSE 8888

ENTRYPOINT ["java","-jar","/ms-config-${APP_VERSION}.jar"]