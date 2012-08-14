package net.sf.testium.executor.webdriver;

import java.util.List;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.selenium.FieldPublisher;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestiumChromeDriver extends ChromeDriver implements FieldPublisher, TestiumDriver {

	private final WebInterface myInterface;
	
	public TestiumChromeDriver(WebInterface anInterface) {
		super();
		myInterface = anInterface;
	}

	@SuppressWarnings("deprecation")
	public TestiumChromeDriver(WebInterface anInterface,DesiredCapabilities capabilities)
	{
		super( capabilities );
		myInterface = anInterface;
	}

	public void addElement(String varName, WebElement element) {
		myInterface.addElement(varName, element);
	}

	public void addElement(String varName, List<WebElement> elements) {
		myInterface.addElement(varName, elements);
	}

	public WebElement getElement(String varName) {
		return myInterface.getElement(varName);
	}

	public BROWSER_TYPE getType()
	{
		return BROWSER_TYPE.CHROME;
	}
}
