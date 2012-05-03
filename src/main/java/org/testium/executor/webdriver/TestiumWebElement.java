package org.testium.executor.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestiumWebElement implements WebElement
{
	private static final int DEFAULT_TIMEOUT = 10;
	
	private WebDriver myDriver;
	private By myFindBy;
	private WebElement myPrerequisite;
	private WebElement myResult;
	private int myTimeout;
	
	public TestiumWebElement( WebDriver aDriver, By aFindBy ) {
		this( aDriver, aFindBy, null, null, DEFAULT_TIMEOUT );
	}

	public TestiumWebElement( WebDriver aDriver,
	                          By aFindBy,
	                          WebElement aPrerequisite,
	                          WebElement aResult,
	                          int aTimeout ) {
		myDriver = aDriver;
		myFindBy = aFindBy;
		myPrerequisite = aPrerequisite;
		myResult = aResult;
		myTimeout = aTimeout;
	}

	// TODO What if result is a list?
	// Use the first?

	public void clear()
	{
		myDriver.findElement(myFindBy).clear();
	}

	public void click()
	{
		myDriver.findElement(myFindBy).click();
	}

	public WebElement findElement(By by)
	{
		return myDriver.findElement(myFindBy).findElement(by);
	}

	public List<WebElement> findElements(By by)
	{
		return myDriver.findElement(myFindBy).findElements(by);
	}

	public String getAttribute(String name)
	{
		return myDriver.findElement(myFindBy).getAttribute(name);
	}

	public String getCssValue(String propertyName)
	{
		return myDriver.findElement(myFindBy).getCssValue(propertyName);
	}

	public Point getLocation()
	{
		return myDriver.findElement(myFindBy).getLocation();
	}

	public Dimension getSize()
	{
		return myDriver.findElement(myFindBy).getSize();
	}

	public String getTagName()
	{
		return myDriver.findElement(myFindBy).getTagName();
	}

	public String getText()
	{
		return myDriver.findElement(myFindBy).getText();
	}

	public boolean isDisplayed()
	{
		return myDriver.findElement(myFindBy).isDisplayed();
	}

	public boolean isEnabled()
	{
		return myDriver.findElement(myFindBy).isEnabled();
	}

	public boolean isSelected()
	{
		return myDriver.findElement(myFindBy).isSelected();
	}

	public void sendKeys(CharSequence... keysToSend)
	{
		myDriver.findElement(myFindBy).sendKeys(keysToSend);
	}

	public void submit()
	{
		myDriver.findElement(myFindBy).submit();
	}
}
