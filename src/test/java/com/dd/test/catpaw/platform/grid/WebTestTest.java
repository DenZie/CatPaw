package com.dd.test.claws.platform.grid;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dd.test.claws.platform.config.ExtendedConfig;
import com.dd.test.claws.platform.html.TestObjectRepository;
import com.dd.test.claws.platform.html.TextField;
import com.dd.test.claws.reports.runtime.WebReporter;

/**
 * Tests different combinations for @Webtest annotation and it's parameters
 */
public class WebTestTest {
    
	/*
	 * Threaded and named session tests
	 */
    
    /**
     * Test named session across two dependent test methods
     **/
	@Test(groups = {"sessionWebTests","functional"})
	@WebTest(sessionName="paypal-help-flow",keepSessionOpen=true)
	public void testNamedSessionAcrossTwoDependentTestMethods_part1() {
	    Grid.open(TestObjectRepository.URL.getValueToUse());
	    WebReporter.log("Sample Unit Test Page (" + Grid.driver().getSessionId() + ")", true, true);
	}
	
    @Test(groups = {"sessionWebTests","functional"},dependsOnMethods="testNamedSessionAcrossTwoDependentTestMethods_part1")
    @WebTest(sessionName="paypal-help-flow",openNewSession=false)
    public void testNamedSessionAcrossTwoDependentTestMethods_part2() {
        //should already be on PayPal main page
        WebReporter.log("Sample Unit Test Page (" + Grid.driver().getSessionId() + ")", true, true);        
        assertTrue(Grid.driver().getTitle().contains("Sample Unit Test Page"), "should be on Sample Unit Test already with this session");
        TextField normalTextField = new TextField(TestObjectRepository.TEXTFIELD_LOCATOR.getValueToUse());
        normalTextField.type("Test");
    }
    
    /**
     * Test dynamically named session across three dependent test methods
     **/
    @Test(groups = {"sessionWebTests","functional"})
	@WebTest(keepSessionOpen=true)
	public void testDynamicallyNamedSessionAcrossThreeTestMethods_part1() {
        Grid.open("http://www.google.com");
        WebReporter.log("Google Main Page (" + Grid.driver().getSessionId() + ")", true, true);	    
	}

    @Test(groups = {"sessionWebTests","functional"}, dependsOnMethods="testDynamicallyNamedSessionAcrossThreeTestMethods_part1")
    @WebTest(openNewSession=false,keepSessionOpen=true)
    public void testDynamicallyNamedSessionAcrossThreeTestMethods_part2() {
        //should already be on google.com
        WebReporter.log("Google Main Page (" + Grid.driver().getSessionId() + ")", true, true);        
        assertTrue(Grid.driver().getTitle().contains("Google"), "shuold be on Google already with this session");
        Grid.open(TestObjectRepository.URL.getValueToUse());
	    WebReporter.log("Sample Unit Test Page", true, true);
    }

    @Test(groups = {"sessionWebTests","functional"}, dependsOnMethods={"testDynamicallyNamedSessionAcrossThreeTestMethods_part1",
            "testDynamicallyNamedSessionAcrossThreeTestMethods_part2"})
    @WebTest(openNewSession=false)
    public void testDynamicallyNamedSessionAcrossThreeTestMethods_part3() {
        //should already be on apple.com
        WebReporter.log("Sample Unit Test Page (" + Grid.driver().getSessionId() + ")", true, true);
        assertTrue(Grid.driver().getTitle().contains("Sample Unit Test Page"), "should be on Google already with this session");
       TextField normalTextField = new TextField(TestObjectRepository.TEXTFIELD_LOCATOR.getValueToUse());
        normalTextField.type("Test");
    }
    
    /**
     * Test un-named session that thinks it wants to use an existing session
     **/
    @Test(groups = {"sessionWebTests","functional"}, expectedExceptions=IllegalArgumentException.class,enabled=false)
    @WebTest(openNewSession=false)
    public void testUnnamedSessionWithoutDependentMethodOpenNewSession_false() {
        //An IllegalArgumentException should have occurred by now
    	//TODO : Fix this test.
    	//IllegalArgumentException is being thrown by afterInvocation(). TestNG doesnt seem to consider
    	//the exceptions that are being thrown by afterInvocation, when it evaluates expectedExceptions
    	//This test would have to be plugged out to prevent false triggers
    }
    
    /**
     * Test ambiguous dynamically named session - a Test with multiple dependencies where both leave sessions open
     **/
    @Test(groups = {"sessionWebTests","functional"})
    @WebTest(keepSessionOpen=true)
    public void testDynamicallyNamedSessionWithAmbiguosDependency_part1() {
        //just spawn a session and leave it open for dependent methods
    }
    
    @Test(groups = {"sessionWebTests","functional"},
            dependsOnMethods="testDynamicallyNamedSessionWithAmbiguosDependency_part1")
    @WebTest(keepSessionOpen=true)
    public void testDynamicallyNamedSessionWithAmbiguosDependency_part2() {
        //just spawn a session and leave it open for dependent methods
    }
    
    @Test(groups = {"sessionWebTests","functional"}, expectedExceptions=IllegalStateException.class,
            dependsOnMethods={"testDynamicallyNamedSessionWithAmbiguosDependency_part1",
                              "testDynamicallyNamedSessionWithAmbiguosDependency_part2"})
    @WebTest(openNewSession=false)
    public void testDynamicallyNamedSessionWithAmbiguosDependency_part3() { 
        //An IllegalStateException should have occurred by now
    }
    
    /**
     * Test support of TestNG's {@link Test} capabilities for multi-threading
     **/
    @Test(groups = {"sessionWebTests","functional"},threadPoolSize = 2,invocationCount = 2,timeOut = 10000)
    @WebTest
    public void testUnamedSessionMultipleThreadsAndInvocations() {
        WebReporter.log("Browser start page", true, true);        
    }
    
	@Test(groups = { "sessionWebTests", "functional" })
	@WebTest(sessionName = "errorFlow", keepSessionOpen = true)
	public void dummySessionCreator() {

	}

	@Test(groups = { "sessionWebTests", "functional" }, expectedExceptions = { RuntimeException.class }, dependsOnMethods = "dummySessionCreator")
	@WebTest(sessionName = "errorFlow", openNewSession = true)
	public void testOpenSessionWithExistingSessionName() {
	}
	
	@Test(groups = "functional")
	@WebTest(additionalCapabilities={"useCaps:true"})
	public void testCapabilityViaAnnotation(){
		assertEquals(Grid.getWebTestConfig().getAdditionalCapabilities().getCapability("useCaps"),"true");
	}

	@Test(testName="testCapabilityViaTestResult",groups = "functional")
	@WebTest
	public void testCapabilityViaTestResult(){
		assertEquals(Grid.getWebTestConfig().getAdditionalCapabilities().getCapability("useCaps"),"true");
	}

	@BeforeMethod
	public void setCapability(ITestResult testResult, Method method){
		Test test = method.getAnnotation(Test.class);
		if (test != null && test.testName().equalsIgnoreCase("testCapabilityViaTestResult")){
			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setCapability("useCaps", "true");
			testResult.setAttribute(ExtendedConfig.CAPABILITIES.getConfig(), dc);
		}
	}

}
