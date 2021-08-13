FROM adoptopenjdk/openjdk16:ubi
RUN mkdir /opt/app
COPY ./target/demo-0.0.1-SNAPSHOT.jar /opt/app/demo-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","/opt/app/demo-0.0.1-SNAPSHOT.jar"]