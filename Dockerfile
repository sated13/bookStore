FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPE ${JAR_FILE} bookStore.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bookStore.jar"]