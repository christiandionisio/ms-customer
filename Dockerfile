FROM openjdk:8
VOLUME /tmp
EXPOSE 9082
ADD ./target/ms-customer-0.0.1-SNAPSHOT.jar ms-customer.jar
ENTRYPOINT ["java","-jar","/ms-customer.jar"]