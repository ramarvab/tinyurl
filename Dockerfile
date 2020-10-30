FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD

COPY pom.xml /build/
WORKDIR /build/

#RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]
RUN mvn dependency:go-offline
COPY src /build/src/

#RUN ["mvn", "package"]
RUN ["mvn", "install", "-Dmaven.test.skip=true"]
FROM openjdk:8-jre-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/tinyurl-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "tinyurl-0.0.1-SNAPSHOT.jar"]