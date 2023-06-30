FROM amazoncorretto:11-alpine
WORKDIR /app
EXPOSE 8080
COPY target/app.jar .
CMD ["java", "-jar", "app.jar"]