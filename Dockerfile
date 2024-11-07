# Step 1: Base image로 OpenJDK를 사용합니다.
FROM openjdk:11
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]