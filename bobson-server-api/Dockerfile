FROM amazoncorretto:17-alpine3.18
WORKDIR app
ARG JAR_FILE=build/libs/bobson-server-api.jar
COPY ${JAR_FILE} application.jar

ENV TZ=Asia/Seoul

HEALTHCHECK --interval=10s --timeout=3s --retries=5 CMD curl --fail http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java",  "-Xmx128m", "-jar", "application.jar"]