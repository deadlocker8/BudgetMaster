FROM tomcat:9-jre8

COPY build/2.2.0/BudgetMaster-v2.2.0.war $CATALINA_HOME/webapps/budgetmaster.war
COPY src/main/resources/config/templates/settings-docker.properties /root/.Deadlocker/BudgetMaster/settings.properties

EXPOSE 8080