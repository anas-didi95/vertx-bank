FROM maven:3.8.5-openjdk-17-slim as build-jar
WORKDIR /workspace
COPY ms-bank-svc/src/ src/
COPY ms-bank-svc/pom.xml pom.xml
RUN mvn clean package -DskipTests

FROM ghcr.io/graalvm/native-image:ol8-java17-22.3.3 as build-nativeimage
WORKDIR /workspace
COPY --from=build-jar /workspace/target/msbanksvc-fat.jar msbanksvc-fat.jar
RUN native-image --static -jar msbanksvc-fat.jar nativeimagejar

FROM alpine:3.18.3
WORKDIR /workspace
COPY --from=build-nativeimage /workspace/nativeimagejar nativeimagejar
COPY --from=build-nativeimage /workspace/reports/ reports/
EXPOSE 8888
ENTRYPOINT ["sh", "-c"]
CMD ["exec ./nativeimagejar run com.anasdidi.msbanksvc.MainVerticle"]
