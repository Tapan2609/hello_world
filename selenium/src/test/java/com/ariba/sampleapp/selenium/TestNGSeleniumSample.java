package com.ariba.sampleapp.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

public class TestNGSeleniumSample {
    static String hubUrl = System.getenv("SAUCE_ENDPOINT");

    public WebDriver initChromeDriver() throws MalformedURLException {

        DesiredCapabilities caps = new DesiredCapabilities();

        // CHROME setting
        // caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        caps.setCapability("tunnelIdentifier", System.getenv("TUNNEL_IDENTIFIER"));
        caps.setBrowserName(BrowserType.CHROME);
        // caps.setCapability("tunnelIdentifier", "sap-intranet");
        // caps.setCapability("parentTunnel", "S-A-P");

        WebDriver chromeDriver = new RemoteWebDriver(new URL(hubUrl), caps);
        System.out.println(chromeDriver.getTitle());
        return chromeDriver;
    }

    @Test
    public void testDeployment() throws MalformedURLException {
        int timeOutInSeconds = 30;
        System.out.println("Testing deployment");
        WebDriver driver = initChromeDriver();
        Calendar timeout = Calendar.getInstance();
        try {
            String serviceUrl = System.getProperty("serviceUrl");
            String textToSearch = System.getProperty("textToSearch");
            timeout.add(Calendar.SECOND, timeOutInSeconds);
            do {
                try {
                    driver.navigate().to(serviceUrl);
                    WebElement element = driver.findElement(By.xpath("//*[contains(text(),'" + textToSearch + "')]"));
                    System.out.println(
                            "Identified element [" + textToSearch + "] in UI with hashcode" + element.hashCode());
                    break;
                } catch (NoSuchElementException e) {
                    System.out.println(
                            "Could not find text [" + textToSearch + "]. Will check again after 200ms interval");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            } while (Calendar.getInstance().before(timeout));
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
