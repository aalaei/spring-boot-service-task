FROM maven:3.9.5-amazoncorretto-21-al2023@sha256:447a36901b8465e207ba830793bce0db9a284118f48fbe03b7dca8c148d5815d AS build
WORKDIR /project
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests -Pprod

#----------------------------------------------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine
RUN apk update &&\
     apk upgrade &&\
     apk add dumb-init &&\
     rm -rf /var/cache/apk/*

WORKDIR /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=build /project/target/*.jar /app/java-application.jar
RUN chown -R javauser:javauser /app
USER javauser
CMD "dumb-init" "java" "-jar" "java-application.jar"
