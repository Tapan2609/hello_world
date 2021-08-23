package com.ariba.sampleapp.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.stereotype.Service;

import com.ariba.sampleapp.service.GreetingService;

import org.springframework.context.annotation.Bean;
import com.ariba.sampleapp.model.Greeting;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class GreetingNet {
    URL url;
    HttpURLConnection conn;

    // the service interacting with Greeting model
    @Autowired
    private GreetingService greetingService;

    @Bean
    public HttpURLConnection httpUrlConnection() {
        if (greetingService.isProduction()) {
            try {

                url = new URL(greetingService.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                greetingService.logger.info("Proxy? " + conn.usingProxy());
                return conn;

            } catch (Exception e) {
                greetingService.logger.info("exception for GreetingNet:" + e.toString());
                return conn;
            }
        }
        return null;
    }

    public HttpURLConnection getConnection() {
        return conn;
    }

    public URL getUrl() {
        return url;
    }

    void Destroy() {
        conn.disconnect();
    }

    protected void finalize() {
        conn.disconnect();
    }

}
