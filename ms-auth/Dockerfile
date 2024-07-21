FROM openjdk:17-jdk-alpine

ARG APP_VERSION

COPY target/ms-auth-${APP_VERSION}.jar /ms-auth-${APP_VERSION}.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/ms-auth-${APP_VERSION}.jar"]