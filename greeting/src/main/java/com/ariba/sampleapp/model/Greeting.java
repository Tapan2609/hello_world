package com.ariba.sampleapp.model;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "server")
public class Greeting {
    @Value("${server.greeting}")
    private String greeting;

    @Value("${server.proxy.url}")
    private String url;

    @Value("${server.proxy.isProduction}")
    private boolean isProduction;

    @Value("${server.proxy.proxyHost}")
    private String proxyHost;

    @Value("${server.proxy.proxyPort}")
    private String proxyPort;

    @Value("${server.proxy.nonProxyHosts}")
    private String nonProxyHosts;

    @Value("${server.orchestrator}")
    private String orchestrator;

    public Greeting() {

    }

    public Greeting(String message) {
        this.greeting = message;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String s) {
        greeting = s;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String s) {
        url = s;
    }

    public boolean isProduction() {
        return isProduction;
    }

    public void setProduction(boolean flag) {
        isProduction = flag;
    }

    public String proxyHost() {
        return proxyHost;
    }

    public String proxyPort() {
        return proxyPort;
    }

    public String nonProxyHosts() {
        return nonProxyHosts;
    }

    public String getOrchestrator() {
        return orchestrator;
    }

    public void setOrchestrator(String s) {
        orchestrator = s;
    }

    @PostConstruct
    public void setProxyProperty() {
        if (isProduction()) {
            System.out.println("Setting system properties for http proxy");
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", proxyPort);
            System.setProperty("http.nonProxyHosts", nonProxyHosts);
            System.setProperty("java.net.useSystemProxies", "true");
        }
    }

}