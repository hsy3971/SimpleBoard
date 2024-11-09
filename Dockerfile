FROM openjdk:11
WORKDIR /spring-boot
# 정확한 JAR 파일 이름을 지정해 주세요. 예: my-app-0.0.1-SNAPSHOT.jar
COPY build/libs/simpleBoard-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/spring-boot/app.jar"]