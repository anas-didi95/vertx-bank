FROM maven:3.8.5-openjdk-17-slim as build-jar
WORKDIR /workspace
COPY ms-bank-svc/src/ src/
COPY ms-bank-svc/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine
WORKDIR /workspace
COPY --from=build-jar /workspace/target/msbanksvc-fat.jar msbanksvc-fat.jar
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar msbanksvc-fat.jar"]
