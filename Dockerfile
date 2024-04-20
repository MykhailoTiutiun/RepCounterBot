FROM tomcat:10.1.11

COPY ./target/RepCounterBot.war /usr/local/tomcat/webapps-javaee/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]