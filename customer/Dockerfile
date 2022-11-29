###########
#  Maven  #
###########################################################################
# In this stage the docker file will copy 'pom.xml' and 'src' directory.  #
# 'mvn clean package' will compile the code and create a jar file.        #
###########################################################################
FROM maven:3.8.6-openjdk-11-slim AS maven
WORKDIR application
COPY customer/pom.xml .
COPY customer/src ./src
RUN mvn clean package -DskipTests

###########
# Builder #
###########################################################################
# In this stage we fetch the '*.jar' file from 'maven-stage' and extract  #
# the content of the JAR file.                                            #
###########################################################################
FROM eclipse-temurin:11-alpine AS builder
WORKDIR application
COPY --from=maven application/target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

###########
#  Main   #
###########################################################################
# In this stage we fetch the extracted content from the 'builder-stage'   #
# and make it runnable application                                        #
###########################################################################
FROM eclipse-temurin:11-alpine
WORKDIR application
COPY --from=builder application/application/ ./
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]