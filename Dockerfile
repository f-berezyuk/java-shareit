FROM eclipse-temurin:21-jre-jammy
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV SPRING_DATASOURCE_URL jdbc:postgresql://db:5432/shareit
ENV SPRING_DATASOURCE_USERNAME shareit
ENV SPRING_DATASOURCE_PASSWORD shareit

# Запустим приложение
ENTRYPOINT ["sh", "-c", "java -Dspring.datasource.url=${SPRING_DATASOURCE_URL} -Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME} -Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD} -jar /app.jar"]