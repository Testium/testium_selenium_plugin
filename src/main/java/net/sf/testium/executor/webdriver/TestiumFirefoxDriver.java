package net.sf.testium.executor.webdriver;

import java.util.List;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.selenium.FieldPublisher;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestiumFirefoxDriver extends FirefoxDriver implements FieldPublisher, TestiumDriver {

	private final WebInterface myInterface;
	
	public TestiumFirefoxDriver( WebInterface anInterface ) {
		super();
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
		return BROWSER_TYPE.FIREFOX;
	}
}
