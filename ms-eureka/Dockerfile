FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-eureka-${APP_VERSION}.jar /ms-eureka-${APP_VERSION}.jar

ENTRYPOINT ["java","-jar","/ms-eureka-${APP_VERSION}.jar"]