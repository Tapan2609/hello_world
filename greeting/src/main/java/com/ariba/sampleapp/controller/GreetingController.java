package com.ariba.sampleapp.controller;

import com.ariba.sampleapp.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.net.*;
import java.time.*;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.*;
import java.io.InputStreamReader;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import io.split.client.SplitClient;
import com.ariba.sampleapp.net.*;
import javassist.ClassPool;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.*;
import java.util.concurrent.TimeUnit ;

@Controller
public class GreetingController {
    private static int count = 0;

    //ClassPool objects hold all the CtClasses.
    static ClassPool classPool = ClassPool.getDefault();

    // the service interacting with Greeting model
    @Autowired
    private GreetingService greetingService;

    /**
     * get container address
     *
     * @param preferIpv4
     * @param preferIPv6
     * @return
     * @throws Exception
     */
    private static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4, boolean preferIPv6) throws Exception {
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

        while (en.hasMoreElements()) {

            NetworkInterface i = (NetworkInterface) en.nextElement();
            if (i.getName().equalsIgnoreCase("en0") || i.getName().equalsIgnoreCase("eth0")) {
                for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
                    InetAddress addr = (InetAddress) en2.nextElement();
                    if (!addr.isLoopbackAddress()) {
                        if (addr instanceof Inet4Address) {
                            if (preferIPv6) {
                                continue;
                            }
                            return addr;
                        }
                        if (addr instanceof Inet6Address) {
                            if (preferIpv4) {
                                continue;
                            }
                            return addr;
                        }
                    }
                }
            }
        }

        throw new Exception("Error: Could not find public ip address!");
    }

    public void demoSplitIO() throws Exception {

        try {
          greetingService.splitClient();
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        } finally {
            // Not closing as split factory is initialized as a singleton bean
            // greetingService.greetingSplit().finalize();
        }

    }
    
    public int demoHttpClients(String httpUrl) throws Exception {
        int statusCode;
        CloseableHttpClient httpClient = HttpClients.createSystem();

        try {
            // Define a postRequest request
            HttpPost postRequest = new HttpPost(httpUrl);
            // Send the request; It will immediately return the response in HttpResponse
            // object if any
            CloseableHttpResponse response = httpClient.execute(postRequest);
            // read the valid error code first
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201) {
                throw new RuntimeException("Failed with demoHttpClients error code : " + statusCode);
            }
            return statusCode;
        } finally {
            // Important: Close the connect
            httpClient.close();
        }
    }

    public int demoHttpClientBuilder(String httpUrl) throws Exception {
        int statusCode;
        HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();

        try {
            // Define a postRequest request
            HttpPost postRequest = new HttpPost(httpUrl);
            // Send the request; It will immediately return the response in HttpResponse
            // object if any
            HttpResponse response = httpClient.execute(postRequest);
            // read the valid error code first
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201) {
                throw new RuntimeException("Failed with demoHttpClientBuilder error code : " + statusCode);
            }
            return statusCode;
        } finally {
            // Important: Close the connect
            httpClient.getConnectionManager().shutdown();
        }
    }

    public int demoNetClient() throws Exception {
        int statusCode;
        HttpURLConnection conn = greetingService.httpURLConnection();
        conn.connect();
        statusCode = conn.getResponseCode();
        // Important: Close the connect
        // greetingService.greetingHTTPController().finalize();
        greetingService.logger.info("Proxy? " + conn.usingProxy());
        return statusCode;

    }

    /**
     * Display the message load
     *
     * @param model this model will be bind to front-end template
     * @return 'greeting' template
     * @throws UnknownHostException
     * @throws SocketException
     */
    @RequestMapping(value = "/proxy", method = RequestMethod.GET)
    public String proxy(Model model) throws UnknownHostException, SocketException {

        if (greetingService.isProduction()) {

            int restapi = 0;
            model.addAttribute("name", greetingService.getGreeting());
            model.addAttribute("orchestrator", greetingService.getOrchestrator());
            try {
                model.addAttribute("ip", System.getenv("CONTAINER_IP"));
                model.addAttribute("count", count);
            } catch (Exception e) {
                model.addAttribute("ip", e.getMessage());
            }

            try {
                demoSplitIO();
                model.addAttribute("splitResponse", "splitFactory bean initialized");
            } catch (Exception e) {
                greetingService.logger.error("exception for splitio call:" + e.getMessage());
                model.addAttribute("splitResponse", e.getMessage());
            }

            try {
                restapi = demoNetClient();
                model.addAttribute("netResponse", restapi);
                greetingService.logger.info("Code for net api call:" + restapi);
            } catch (Exception e) {
                greetingService.logger.error("exception for net api call:" + e.getMessage());
                model.addAttribute("netResponse", e.getMessage());
            }

            try {
                restapi = demoHttpClients(greetingService.getUrl());
                model.addAttribute("httpclientsResponse", restapi);
                greetingService.logger.info("Code for httpclients call:" + restapi);
            } catch (Exception e) {
                greetingService.logger.error("exception for httpclients call:" + e.getMessage());
                model.addAttribute("httpclientsResponse", e.getMessage());
                greetingService.logger.info("exception for httpclients call:" + e.toString());
            }

            try {
                restapi = demoHttpClientBuilder(greetingService.getUrl());
                model.addAttribute("httpclientbuilderResponse", restapi);
                greetingService.logger.info("Code for httpclientbuilder call:" + restapi);
            } catch (Exception e) {
                greetingService.logger.error("exception for httpclientbuilder call:" + e.getMessage());
                model.addAttribute("httpclientbuilderResponse", e.getMessage());
                greetingService.logger.info("exception for httpclientbuilder call:" + e.toString());
            }

        }
        return "html/greeting/proxy";

    }

    /**
     * Display the message load
     *
     * @param model this model will be bind to front-end template
     * @return 'greeting' template
     * @throws UnknownHostException
     * @throws SocketException
     */
    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String greeting(Model model) throws UnknownHostException, SocketException {
        // bind model to template
        model.addAttribute("name", greetingService.getGreeting());
        model.addAttribute("orchestrator", greetingService.getOrchestrator());

        try {
            model.addAttribute("ip", System.getenv("CONTAINER_IP"));
            model.addAttribute("count", ++count);
        } catch (Exception e) {
            model.addAttribute("ip", e.getMessage());
        }

        return "html/greeting/greeting";
    }

    @RequestMapping(value = "/oom-metaspace", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void metaSpace() throws Exception {
        greetingService.logger.info("Please set Greetings memory to 16GB to generate OOM-METASPACE error, otherwise it'll generate OOM-HEAP error");
        try {
            for (int i = 0; i < Integer.MAX_VALUE ; i++) {
                Class c = classPool.makeClass("com.ariba.sampleapp.controller.generated" + i).toClass();
                greetingService.logger.info(c.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value = "/oom-heap", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void heapOOM() throws UnknownHostException, SocketException {
        try {
            generateOOM();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generateOOM() throws Exception {
        int iteratorValue = 20;
        greetingService.logger.info("OOM test for Heap memory started");
        for (int outerIterator = 1; outerIterator < 20; outerIterator++) {
            int loop1 = 2;
            int[] memoryFillIntVar = new int[iteratorValue];
            // fill memoryFillIntVar array in loop..
            do {
                memoryFillIntVar[loop1] = 0;
                loop1--;
            } while (loop1 > 0);
            iteratorValue = iteratorValue * 5;
            greetingService.logger.info("\nRequired Memory for next loop: " + iteratorValue);
        }
    }

    /**
     * Display the message load
     *
     * @param model this model will be bind to front-end template
     * @return 'greeting' template
     * @throws UnknownHostException
     * @throws SocketException
     */
    @RequestMapping(value = "/external-access", method = RequestMethod.GET)
    public String externalAccess(Model model) throws UnknownHostException, SocketException {
        extAccess(model, "http://example.com");
        return "html/greeting/greeting";
    }

    @RequestMapping(value = "/external-access-ip", method = RequestMethod.GET)
    public String externalAccessIP(Model model) throws UnknownHostException, SocketException {
        extAccess(model, "http://93.184.216.34");
        return "html/greeting/greeting";
    }
    
    private void extAccess(Model model, String endpoint) {
        try {
            model.addAttribute("ip", System.getenv("CONTAINER_IP"));
            model.addAttribute("count", ++count);
            Instant startTime = Instant.now();
            greetingService.logger.info(startTime + " Initiating call to ${endpoint}\n");
            URL url = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            Instant endTime = Instant.now();
            long timeElapsed = Duration.between(startTime, endTime).toMillis();
            greetingService.logger.info(endTime + " Call completed. Response " + status + ", Time taken " + timeElapsed + "ms\n");
            model.addAttribute("name", "Example.com access time [" + (timeElapsed) + "]ms" );
        } catch (Exception e) {
            model.addAttribute("name", "Exception during external access");
            model.addAttribute("ip", e.getMessage());
        }
    }
    
    /**
     * Display the message load
     *
     * @param model this model will be bind to front-end template
     * @return 'greeting' template
     * @throws UnknownHostException
     * @throws SocketException
     */
    @RequestMapping(value = "/oauth-access", method = RequestMethod.GET)
    public String oauthAccess(Model model) throws UnknownHostException, SocketException {
        try {
            model.addAttribute("ip", System.getenv("CONTAINER_IP"));
            model.addAttribute("count", ++count);
            URL url = new URL("https://api.ariba.com/v2/oauth/token");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", System.getenv("OauthAuthorization"));
            StringBuilder postData = new StringBuilder();
            postData.append("grant_type=client_credentials");
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            con.setDoOutput(true);
            Instant startTime = Instant.now();
            greetingService.logger.info(startTime + " Initiating call to api.ariba.com\n");
            OutputStream output = null;
            try {
                output = con.getOutputStream();
                output.write(postDataBytes);
            } finally {
                if(output != null) {
                    try {
                        output.close();      
                    } catch(IOException e) {
                        greetingService.logger.error("Exception occured: " + e.getMessage());
                    }
                }                 
            }
            
            BufferedReader br = null;
            if (con.getResponseCode() == 200) {
                InputStream input = null;
                try {
                    input = con.getInputStream();
                    br = new BufferedReader(new InputStreamReader(input));
                    String strCurrentLine;
                    while ((strCurrentLine = br.readLine()) != null) {
                           greetingService.logger.info(strCurrentLine);
                    }
                } finally {
                    if(input != null) {
                        try {
                            input.close();      
                        } catch(IOException e) {
                            greetingService.logger.error("Exception occured: " + e.getMessage());
                        }
                    }
                }
            } else {
                InputStream error = null;
                try {
                    error = con.getErrorStream();
                    br = new BufferedReader(new InputStreamReader(error));
                    String strCurrentLine;
                    while ((strCurrentLine = br.readLine()) != null) {
                           greetingService.logger.info(strCurrentLine);
                    }
                } finally {
                    if(error != null) {
                        try {
                            error.close();     
                        } catch(IOException e) {
                            greetingService.logger.error("Exception occured: " + e.getMessage());
                        }
                    }
                }
            }
            br.close();
            int status = con.getResponseCode();
            Instant endTime = Instant.now();
            long timeElapsed = Duration.between(startTime, endTime).toMillis();
            con.disconnect();
            greetingService.logger.info(endTime + " Call completed. Response [%d], Time taken [%d]ms\n", status, 
                    timeElapsed);
            model.addAttribute("name", "api.ariba.com/v2/oauth/token access time [" + (timeElapsed) + "]ms" );
        }  catch (Exception e) {
            model.addAttribute("name", "Exception during oauth access");
            model.addAttribute("ip", e.getMessage());
        }
        return "html/greeting/greeting";
    }

    @RequestMapping(value = "/readfile", method = RequestMethod.GET)
    public String readFile(Model model) throws UnknownHostException, SocketException {

        String ftpUrl = System.getenv("NOMAD_JOB_NAME") + "-ftp.service";
        int defaultPort = 21;
        String fileContent = null;
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(ftpUrl, defaultPort);
            ftpClient.login("files", "test");
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory("/home/files");
            InputStream in = ftpClient.retrieveFileStream("readme.txt");
            BufferedInputStream inbf = new BufferedInputStream(in);

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inbf.read(buffer)) != -1) {
                fileContent = new String(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            greetingService.logger.error(e.getMessage());
            model.addAttribute("ip", e.getMessage());
        }
        
        if(fileContent != null) {
            if(fileContent.contains("FTP in cobalt environment")) {
                greetingService.logger.info("Downloaded correct file.");
                model.addAttribute("content", "Downloaded correct file.");
            } else {
                greetingService.logger.info("Downloaded incorrect file.");
                model.addAttribute("content", "Downloaded incorrect file.");
            }
        }

        return "html/greeting/content";
    }

    @RequestMapping(value = "/create-defunct", method = RequestMethod.GET)
    public String createDefunctProcess(Model model) throws UnknownHostException, SocketException {

        // bind model to template
        model.addAttribute("name", "Performing Defunct Testing");
        model.addAttribute("orchestrator", greetingService.getOrchestrator());

        try {
            model.addAttribute("ip", System.getenv("CONTAINER_IP"));
            model.addAttribute("count", ++count);
        } catch (Exception e) {
            model.addAttribute("ip", e.getMessage());
        }

        try {
            createDefunctProcessForTest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "html/greeting/greeting";
    }


    public void createDefunctProcessForTest() throws RuntimeException, UnknownHostException, SocketException, Exception {
        String commandToRunFinal = "$(which sleep) 1 & exec $(which sleep) 20";
        greetingService.logger.info(String.format("executing command [%s] in shell", commandToRunFinal));
        String[] commands = { "/bin/sh", "-c",  commandToRunFinal};
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands);
        greetingService.logger.info("Return");
    }
}
