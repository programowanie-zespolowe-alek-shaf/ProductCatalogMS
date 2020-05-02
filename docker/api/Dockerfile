FROM maven:3.6.3-openjdk-11
WORKDIR app
COPY pc-application ./pc-application
COPY pc-common ./pc-common
COPY pc-mysql ./pc-mysql
COPY pom.xml ./

ENV MYSQL_CUSTOMER_DB_HOST name
ENV MYSQL_CUSTOMER_DB_PORT 3306
ENV MYSQL_CUSTOMER_DATABASE product
ENV MYSQL_CUSTOMER_DB_USERNAME product
ENV MYSQL_CUSTOMER_DB_PASSWORD product
ENV EUREKA_HOST name
ENV EUREKA_PORT 8761

RUN mvn clean install -DskipTests -P prod
RUN mv pc-application/target/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]
