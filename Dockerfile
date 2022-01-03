FROM adoptopenjdk:15-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./api/target/comment-api-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "comment-api-1.0-SNAPSHOT.jar"]