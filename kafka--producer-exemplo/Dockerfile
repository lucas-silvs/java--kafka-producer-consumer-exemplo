FROM gradle:7.5.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon


FROM amazoncorretto:17.0.9-alpine

LABEL "author"="Lucas Santos"
LABEL "email"="lucas.parkl@outlook.com"

COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
WORKDIR /app

ENV JAVA_OPTS "$JAVA_OPTS \
    -XX:+UseParallelGC \
    -XX:ActiveProcessorCount=2 \
    -XX:MaxRAMPercentage=75 \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    -Duser.timezone=America/Fortaleza"

ENTRYPOINT [ "sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
EXPOSE 5000 9090
