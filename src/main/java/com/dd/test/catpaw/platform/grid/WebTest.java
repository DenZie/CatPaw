package com.dd.test.catpaw.platform.grid;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * interface creates annotation WebTest and specified parameters<br>
 * Annotation @Webtest will let Grid know to open RC and browser instance, start session
 *
 */
@Retention(RUNTIME)
@Target( { CONSTRUCTOR, METHOD, TYPE })
public @interface WebTest {
	
	/**
	 * Establish browser profile to use. Will default to "firefox".
	 * <b>Example</b>
	 * <pre>
	 * &#64;Test()
	 * &#64;WebTest(browser="*iexplore")   
	 * public void webtest2()  {
	 *   Grid.selenium().open("http://paypal.com");
	 * }
	 * </pre>
	 */
	String browser() default "";
	
	
	/**
	 * Keep the session open or not <code>true/false</code>
	 * <b>default</b>: false</br>
	 * <b>Example</b>
	 * <pre>
	 * &#64;Test()
	 * &#64;WebTest(keepSessionOpen=false)   
	 * public void webtest2()  {
	 *	 Grid.selenium().open("http://paypal.com");
	 * }
	 * </pre>
	 */
	boolean keepSessionOpen() default false;
	
	
	/**
	 * Force a new session to open or not <code>true/false</code>
     * <b>default</b>: true</br>
	 * <pre>
	 * &#64;Test()
	 * &#64;WebTest(openNewSession=false)   
	 * public void webtest2()  {
	 *   Grid.selenium().open("http://paypal.com");
	 * }
	 * </pre>	 
	 */
	boolean openNewSession() default true;
	
	 /**
     * Establish a name for this browser session or switch to a session left open 
     * with the same name. By default, a dynamic name will be assigned. Use this in
     * conjunction with {@link #keepSessionOpen()} and {@link #openNewSession()}. 
     * Session names are class aware. In other words, you can not share sessions 
     * across different classes.
     * <pre>
     * &#64;Test()
     * &#64;WebTest(sessionName="buyerSession",keepSessionOpen=true)   
     * public void testNavigateTo()  {
     *    Grid.selenium().open("http://paypal.com");
     * }
     *
     * &#64;Test()
     * &#64;WebTest(sessionName="buyerSession",openNewSession=false)   
     * public void testBuyerLoginTo()  {
     *    // Do  login steps
     * }
     * </pre>
     */
	String sessionName() default "";
	
	/**
	 * Provide additional capabilities that you may wish to add as a name value pair.
	 * <pre>
	 * {@literal @}Test
	 * {@literal @}WebTest(additionalCapabilities={"key1:value1","key2:value2"})
	 * public void testMethod(){
	 * 		//UI navigation steps 
	 * }
	 * </pre>
	 */
	String[] additionalCapabilities() default {};
}
