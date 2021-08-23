package com.ariba.sampleapp.model;


import com.ariba.sampleapp.MainAppTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GreetingTest for the Greeting model.
 */

public class GreetingTest extends MainAppTest {

    @Autowired
    private Greeting greeting;

    @Before
    public void setUp() {

    }

    /**
     * Test for greeting
     */
    @Test
    public void testGreeting() {
        greeting.setProduction(true);
        greeting.setGreeting("Hello");
        String testGreeting = greeting.getGreeting();
        Assert.assertEquals(testGreeting.toString(), "Hello");
        greeting.setOrchestrator("Hello");
        String testOrchestrator = greeting.getOrchestrator();
        Assert.assertEquals(testOrchestrator.toString(), "Hello");
    }

    @After
    public void tearDown() {

    }
}
