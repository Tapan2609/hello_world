package com.ariba.sampleapp.net;



import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;
import io.split.client.SplitFactoryBuilder;

import java.lang.Exception;
import org.springframework.stereotype.Service;

import com.ariba.sampleapp.service.GreetingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;

@Service
public class GreetingSplit {

    private SplitClientConfig config;

    private SplitClient client;

    SplitFactory splitFactory;

    // the service interacting with Greeting model
    @Autowired
    private GreetingService greetingService;

    @Bean
    public SplitFactory splitFactory() throws Exception {

        if (greetingService.isProduction()) {
            try {

                greetingService.logger.info("splitFactory bean initialization");
                if ((System.getProperty("https.proxyHost")) == null || (System.getProperty("https.proxyPort")) == null)
                    return null;

                Path tempFile = Files.createTempFile("tempMockSplit", ".yml");
                InputStream input = null;
                String path = "";
                try {
                    input = GreetingSplit.class.getClassLoader().getResourceAsStream("samplesplit.yml");
                    Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);    
                    path = tempFile.toAbsolutePath().toString();
                } finally {
                    if(input != null) {
                        try {
                            input.close();      
                        } catch(IOException e) {
                            greetingService.logger.error("Exception occured: " + e.getMessage());
                        }
                   }
                }

                config = SplitClientConfig.builder().proxyHost(System.getProperty("https.proxyHost"))
                        .proxyPort(Integer.parseInt(System.getProperty("https.proxyPort")))
                        // set refresh rate to max int
                        .setBlockUntilReadyTimeout(10000).splitFile(path).segmentsRefreshRate(2147483647)
                        .featuresRefreshRate(2147483647)
                        // .enableDebug()
                        .build();

                splitFactory = SplitFactoryBuilder.build("APIKey", config);
                client = splitFactory.client();
                // client.blockUntilReady();
                return splitFactory;

            } catch (IOException | URISyntaxException e) {
                throw new Exception(e.getMessage());
            }
        }

        return null;
    }
    
    public SplitClient client() {
        return client;
    }

    void Destroy() {
        client.destroy();
    }

    protected void finalize() {
        client.destroy();
    }

}
