FROM eclipse-temurin:17-jre-alpine

RUN apk update && apk upgrade && \
    rm -rf /var/cache/apk

ARG APP_DIR=/BudgetMaster

RUN mkdir -p $APP_DIR
RUN mkdir -p /root/.Deadlocker/BudgetMaster

COPY BudgetMasterServer/build/2.16.1/BudgetMasterServer-v2.16.1.jar /BudgetMaster/BudgetMaster.jar
COPY BudgetMasterServer/src/main/resources/config/templates/settings-docker.properties /root/.Deadlocker/BudgetMaster/settings.properties
RUN echo "server.port=9000" > ~/.Deadlocker/BudgetMaster/settings.properties

EXPOSE 9000

WORKDIR $APP_DIR

CMD ["java", "-jar", "BudgetMaster.jar"]
