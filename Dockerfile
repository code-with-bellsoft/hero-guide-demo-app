FROM bellsoft/liberica-runtime-container:jdk

RUN apk add curl

WORKDIR /app
ARG project
ENV project=${project}
COPY .mvn/ .mvn/
COPY mvnw ./
COPY pom.xml ./
COPY chat-api ./chat-api
COPY bot-assistant ./bot-assistant

RUN ./mvnw package -pl ${project} -am -Dmaven.test.skip=true

ENTRYPOINT java -jar ${project}/target/${project}-1.0-SNAPSHOT.jar
