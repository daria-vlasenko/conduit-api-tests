FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /tests

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src

ENV BASE_URL=https://node-express-conduit.appspot.com

CMD ["mvn", "-B", "clean", "test"]
