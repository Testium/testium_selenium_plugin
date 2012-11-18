package net.sf.testium.selenium;

import org.openqa.selenium.WebDriver;

public interface WebDriverInterface
{
	/**
	 * @return the webdriver of this interface. Can be null.
	 */
	public WebDriver getDriver();
	
	/**
	 * @return the base URL of the WebSite under test
	 */
	public String getBaseUrl();
}
