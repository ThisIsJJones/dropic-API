package com.example.dropic.app.config;

import org.apache.catalina.Context;
import org.apache.catalina.authenticator.BasicAuthenticator;
import org.apache.catalina.realm.MessageDigestCredentialHandler;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.apache.tomcat.util.descriptor.web.NamingResources;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.apache.tomcat.util.descriptor.web.WebXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class DropicWebServerFactory extends TomcatServletWebServerFactory {

    private static final int DEFAULT_STORE_NUMBER = 7703;

    private static final String URL_PROTOCOL_PROGRESS = "jdbc:datadirect:openedge:";
    private static final String URL_DOMAIN = "sys" + DEFAULT_STORE_NUMBER + ".st.menards.net";
    private static final String URL_FORMAT = URL_PROTOCOL_PROGRESS + "//" + URL_DOMAIN + ":%d;databaseName=%s";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
        tomcat.enableNaming();

        // Setup the server credentials for development.
        String user = "username";
        tomcat.addUser(user, "howdy");
        tomcat.addRole(user, "ADMIN");

        return super.getTomcatWebServer(tomcat);
    }

    @SuppressWarnings("CheckStyle")
    @Override
    protected void postProcessContext(Context context) {
        NamingResources resources = context.getNamingResources();
        resources.addResource(createResource("jdbc/storeHandyDb", "handy", 22802));
        resources.addResource(createResource("jdbc/storeMenardDb", "menard", 22801));

        setCredentialHandler(context);

        // Add valve so the embedded tomcat get the request and authenticates it.
        context.getPipeline().addValve(new BasicAuthenticator());
    }

    private ContextResource createResource(String resourceName, String dbName, int port) {
        ContextResource resource = new ContextResource();
        resource.setName(resourceName);
        resource.setType(DataSource.class.getName());
        resource.setProperty("driverClassName", "com.ddtek.jdbc.openedge.OpenEdgeDriver");
        resource.setProperty("url", String.format(URL_FORMAT, port, dbName));
        resource.setProperty("username", "ExampleUSERNAME");
        resource.setProperty("password", "ExamplePASSWORD");
        return resource;
    }

    private void setCredentialHandler(final Context context) {
        MessageDigestCredentialHandler messageDigestCredentialHandler = new MessageDigestCredentialHandler();
        try {
            messageDigestCredentialHandler.setAlgorithm("md5");
        } catch (NoSuchAlgorithmException e) {
            log.error("Unable to set algorithm", e);
        }
        context.getRealm().setCredentialHandler(messageDigestCredentialHandler);
    }


}