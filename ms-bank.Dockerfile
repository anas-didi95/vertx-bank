FROM maven:3.8.5-openjdk-17-slim as build-jar
WORKDIR /workspace
COPY ms-bank/src/ /workspace/src/
COPY ms-bank/pom.xml /workspace/pom.xml
RUN mvn clean package -DskipTests

FROM ghcr.io/graalvm/native-image:ol8-java17-22.3.3 as build-nativeimage
WORKDIR /workspace
COPY --from=build-jar /workspace/target/ms-bank-1.0.0-SNAPSHOT-fat.jar /workspace/ms-bank-1.0.0-SNAPSHOT-fat.jar
RUN native-image --static -jar ms-bank-1.0.0-SNAPSHOT-fat.jar ms-bank

FROM alpine:3.18.3
WORKDIR /workspace
COPY --from=build-nativeimage /workspace/ms-bank /workspace/ms-bank
COPY --from=build-nativeimage /workspace/reports/ /workspace/reports/
EXPOSE 8888
ENTRYPOINT ["sh", "-c"]
CMD ["exec ./ms-bank run com.anasdidi.ms_bank.MainVerticle"]
