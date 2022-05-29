FROM tomcat:9-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*
COPY BudgetMasterServer/build/2.10.0/BudgetMasterServer-v2.10.0.war $CATALINA_HOME/webapps/ROOT.war
COPY BudgetMasterServer/src/main/resources/config/templates/settings-docker.properties /root/.Deadlocker/BudgetMaster/settings.properties

EXPOSE 8080