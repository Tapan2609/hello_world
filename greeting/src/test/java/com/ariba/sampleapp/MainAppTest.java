package com.ariba.sampleapp;

/**
 * Abstract Test Class
 * - Common functions, initialization code etc. can be added here
 */

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("hsql")
public abstract class MainAppTest {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

}