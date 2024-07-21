FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-gateway-${APP_VERSION}.jar /ms-gateway-${APP_VERSION}.jar

ENTRYPOINT ["java","-jar","/ms-gateway-${APP_VERSION}.jar"]