FROM amazoncorretto:21-alpine

WORKDIR /app

COPY target/spring-bot-0.0.1-SNAPSHOT.jar /app/spring-bot-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "spring-bot-0.0.1-SNAPSHOT.jar"]
