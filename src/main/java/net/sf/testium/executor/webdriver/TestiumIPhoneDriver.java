package net.sf.testium.executor.webdriver;

import java.util.List;

import net.sf.testium.selenium.FieldPublisher;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.iphone.IPhoneDriver;

public class TestiumIPhoneDriver extends IPhoneDriver implements FieldPublisher//, TestiumDriver
{

	private final WebInterface myInterface;

	public TestiumIPhoneDriver( WebInterface anInterface ) throws Exception {
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

//	public BROWSER_TYPE getType()
//	{
//		return BROWSER_TYPE.IPHONE;
//	}
}
