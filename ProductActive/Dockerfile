FROM openjdk:11
VOLUME /tmp
EXPOSE 8085
ADD ./target/ProductActive-0.0.1-SNAPSHOT.jar ms-productactive.jar
ENTRYPOINT ["java", "-jar","/ms-productactive.jar"]

