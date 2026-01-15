FROM eclipse-temurin:25-alpine-3.23
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=risk-manager-main/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
