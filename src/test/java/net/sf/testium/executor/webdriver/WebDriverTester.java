package net.sf.testium.executor.webdriver;

import java.util.List;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import junit.framework.TestCase;

public class WebDriverTester extends TestCase 
{
	@Test
	public void testcase_theUserShouldBeAbleToTypeInQueryTerms() {
//		WebDriver driver = new FirefoxDriver();
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.google.com");
		WebElement queryField = driver.findElement(By.name("q"));
		queryField.sendKeys("cats");
		queryField.submit();	
		sleep( 1000 );
//		assertEquals("Google", driver.getTitle());
		assertEquals("cats - Google zoeken", driver.getTitle());
		
//		driver.close();
//		driver = null;
//		
//		driver = new FirefoxDriver();
//		driver.get("http://www.google.com");

System.out.println( "Check 1" );
		driver.navigate().back();
sleep( 1000 );
System.out.println( "Check 2" );
assertEquals("Google", driver.getTitle());
		
		List<WebElement> queryFields = driver.findElements(By.name("q"));
System.out.println( "Check 3" );
		WebElement elm = queryFields.get(0);
System.out.println( "Check 4" );
		elm.clear();
System.out.println( "Check 4a" );
		elm.sendKeys("dogs");
System.out.println( "Check 5" );
		elm.submit();	
System.out.println( "Check 6" );
sleep( 1000 );
		assertEquals("dogs - Google zoeken", driver.getTitle());
		
		driver.close();
	}

	/**
	 * 
	 */
	private void sleep( long millis )
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			fail( e.getLocalizedMessage() );
			e.printStackTrace();
		}
	}
}
