FROM tomcat:10.1.18-jdk17

WORKDIR /usr/local/tomcat/

# Copy server configuration
#COPY ./server.xml ./conf/

# Copy my war file
COPY ./target/transactions-0.0.1.war ./webapps/ws.pgg.rest.transactions.war

EXPOSE 8080
