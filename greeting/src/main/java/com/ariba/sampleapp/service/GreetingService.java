package com.ariba.sampleapp.service;

import com.ariba.sampleapp.model.Greeting;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

import java.net.HttpURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import com.ariba.sampleapp.net.*;
import io.split.client.SplitClient;
import org.apache.logging.log4j.*;

/**
 * This service talks to Greeting model and is able to get/update/reload
 * Greeting model
 */
@EnableConfigurationProperties(Greeting.class)
@Service
public class GreetingService {
    /**
     * Initializing the StatsDClient
     */
    private static final StatsDClient statsd = new NonBlockingStatsDClient(
            "sampleapp.greetingservice",          /* prefix to any stats; may be null or empty string */
            System.getenv("NOMAD_IP_cobalt"),     /* common case: NOMAD_IP_cobalt  */
            8125,                                 /* port */
            new String[]{"role:sampleapp"}            /* DataDog extension: Constant tags, always applied */
    );
    

    public final Logger logger = LogManager.getLogger(Greeting.class);
    
    @Autowired
    private Greeting greeting;
    
    @Autowired
    private GreetingSplit greetingSplit;
    
    @Autowired
    private GreetingNet greetingNet;
   
    public String getGreeting() {
        /**
         * getGreeting counter to be pushed down to udp
         * on port 8125 via the statsd client.
         */
        statsd.incrementCounter("getGreeting");
        return greeting.getGreeting();
    }

    public void updateGreeting(String s) {

        /**
         * updateGreeting counter to be pushed down to udp
         * on port 8125 via the statsd client.
         */
        statsd.incrementCounter("updateGreeting");
        greeting.setGreeting(s);
    }

    public String getOrchestrator() {
        /**
         * getOrchestrator counter to be pushed down to udp on port 8125 via the statsd
         * client.
         */
        statsd.incrementCounter("getOrchestrator");
        return greeting.getOrchestrator();
    }

    public void updateOrchestrator(String s) {

        /**
         * updateOrchestrator counter to be pushed down to udp on port 8125 via the
         * statsd client.
         */
        statsd.incrementCounter("updateOrchestrator");
        greeting.setOrchestrator(s);
    }

    public String getUrl() {
        /**
         * getGreeting counter to be pushed down to udp
         * on port 8125 via the statsd client.
         */
        return greeting.getUrl();
    }
    
    public boolean isProduction() {
        return greeting.isProduction();
    }
    
    public SplitClient splitClient() {
        return greetingSplit.client();
    }
    
    public HttpURLConnection httpURLConnection() {
        return greetingNet.getConnection();
    }
}
