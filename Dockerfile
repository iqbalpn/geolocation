FROM maven:3.9.7-eclipse-temurin-21 AS build
#COPY ./ /usr/src/app/
COPY mvnw /usr/src/app/
#COPY .mvn /usr/src/app/.mvn
COPY pom.xml /usr/src/app/
COPY src /usr/src/app/src
RUN mvn -f /usr/src/app/pom.xml -B de.qaware.maven:go-offline-maven-plugin:resolve-dependencies
WORKDIR /usr/src/app
RUN mvn -DskipTests=true -Dquarkus.package.type=mutable-jar -B de.qaware.maven:go-offline-maven-plugin:resolve-dependencies clean package
#RUN sh /usr/src/app/mvnw clean install

FROM registry.access.redhat.com/ubi8/openjdk-21:1.18
#WORKDIR /app
WORKDIR /usr/src/app
#COPY --from=build /app/target/*-runner.jar /usr/src/app/application.jar

COPY --chown=185 --from=build /usr/src/app/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=build /usr/src/app/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=build /usr/src/app/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=build /usr/src/app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]




