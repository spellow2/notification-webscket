FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} websocket_server.jar
ENTRYPOINT ["java","-XX:-MaxFDLimit","-jar","/websocket_server.jar"]
