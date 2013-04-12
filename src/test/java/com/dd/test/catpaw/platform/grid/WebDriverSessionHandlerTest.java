package com.dd.test.claws.platform.grid;

import static org.testng.Assert.assertTrue;

import java.util.concurrent.ExecutionException;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.config.ClawsConfig;
import com.dd.test.claws.platform.config.ClawsConfig.ClawsConfigProperty;

public class WebDriverSessionHandlerTest {
	
	@Test (groups = "unit",expectedExceptions=RuntimeException.class, expectedExceptionsMessageRegExp = "WebDriverSessionHandler is already started" )
	@WebTest
	public void testStartWebDriverSession() throws ExecutionException{
		
		WebDriverSessionHandler m = new WebDriverSessionHandler(Grid.driver());
		m.start();
		m.start();
			
	}
	
	@Test(groups = "unit",expectedExceptions=RuntimeException.class, expectedExceptionsMessageRegExp = "\\QPlease call startSession() before calling endSession()\\E" )
	@WebTest
	public void testEndWebDriverSession() throws ExecutionException{
		WebDriverSessionHandler m = new WebDriverSessionHandler(Grid.driver());
		m.stop();
		
	}
	
	
	@Test(groups="functional")
	@WebTest
	public void testWebDriverSessionHandler() throws ExecutionException, InterruptedException {

		Grid.driver().get(ClawsConfig.getConfigProperty(ClawsConfigProperty.PAYPAL_URL));
		
		Grid.driver().findElementById("login_email").sendKeys("beamdaddy@paypal.com");
		Grid.driver().findElementById("login_password").sendKeys("11111111");
		Grid.driver().findElementByName("submit.x").click();
		
		WebDriverSessionHandler m = new WebDriverSessionHandler(Grid.driver());
		
		m.start();

		Thread.sleep(1000 *60*4);

		m.stop();
		
		Grid.driver().findElementByLinkText("Send Money").click();
		
		assertTrue(Grid.driver().getTitle().equals("Send Money - PayPal"));
		
		

	}


}
