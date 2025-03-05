FROM openjdk:17-jdk-slim
WORKDIR /app

COPY week1_springboot-0.0.1-SNAPSHOT.war app.war

RUN mkdir -p /app/src/main/resources/static/uploads/private
RUN mkdir -p /app/src/main/resources/downloads
RUN mkdir -p /app/src/main/webapp/uploads/private

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.war"]
