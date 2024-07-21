FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-gateway-${APP_VERSION}.jar /ms-gateway-${APP_VERSION}.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/ms-gateway-${APP_VERSION}.jar"]