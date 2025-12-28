
FROM eclipse-temurin:21-jdk-alpine


WORKDIR /app


COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .


COPY . .


RUN chmod +x ./gradlew


RUN ./gradlew build -x test


EXPOSE 8080


CMD ["java", "-jar", "build/libs/ktor-0.0.1-all.jar"]
