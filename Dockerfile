FROM openjdk:8
ENV SPRING_PROFILES_ACTIVE prod
VOLUME /tmp
EXPOSE 9082
ADD ./target/ms-customer-0.0.1-SNAPSHOT.jar ms-customer.jar
ENTRYPOINT ["java","-jar","/ms-customer.jar"]