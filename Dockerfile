FROM tomcat:9-jdk11

RUN rm -rf /usr/local/tomcat/webapps/*
COPY build/2.5.1/BudgetMaster-v2.5.1.war $CATALINA_HOME/webapps/ROOT.war
COPY src/main/resources/config/templates/settings-docker.properties /root/.Deadlocker/BudgetMaster/settings.properties

EXPOSE 8080