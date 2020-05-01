FROM maven:latest
WORKDIR app
COPY pc-application ./pc-application
COPY pc-common ./pc-common
COPY pc-mysql ./pc-mysql
COPY pom.xml ./
RUN mvn clean install -DskipTests -P prod
RUN mv pc-application/target/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]
