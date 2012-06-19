package net.sf.testium.executor.webdriver;

import java.util.List;

import net.sf.testium.selenium.FieldPublisher;
import net.sf.testium.selenium.SimplePageElement;
import net.sf.testium.selenium.SimpleElementList;
import net.sf.testium.selenium.SmartWebElement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.iphone.IPhoneDriver;

public class TestiumIPhoneDriver extends IPhoneDriver implements FieldPublisher {

	private final WebInterface myInterface;

	public TestiumIPhoneDriver( WebInterface anInterface ) throws Exception {
		super();
		myInterface = anInterface;
	}

	@Override
	public SimpleElementList findElements(By by) {
		List<WebElement> elms = by.findElements(this);
		if ( elms instanceof SimpleElementList ) {
			return (SimpleElementList) elms;
		}
		return new SimpleElementList( by, myInterface, elms );
	}

	@Override
	public SmartWebElement findElement(By by) {
		WebElement elm = by.findElement(this);
		if ( elm instanceof SmartWebElement ) {
			return (SmartWebElement) elm;
		}
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SmartWebElement findElementById(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementById(using);
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SimpleElementList findElementsById(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsById(using);
		return new SimpleElementList( by, myInterface, elms );
	}

	@Override
	public SmartWebElement findElementByXPath(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByXPath(using);
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SimpleElementList findElementsByXPath(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByXPath(using);
		return new SimpleElementList( by, myInterface, elms );
	}

	@Override
	public SmartWebElement findElementByTagName(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByTagName(using);
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SimpleElementList findElementsByTagName(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByTagName(using);
		return new SimpleElementList( by, myInterface, elms );
	}

	@Override
	public SmartWebElement findElementByCssSelector(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByCssSelector(using);
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SimpleElementList findElementsByCssSelector(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByCssSelector(using);
		return new SimpleElementList( by, myInterface, elms );
	}

	@Override
	public SmartWebElement findElementByName(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByName(using);
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SimpleElementList findElementsByName(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByName(using);
		return new SimpleElementList( by, myInterface, elms );
	}

	@Override
	public SmartWebElement findElementByLinkText(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByLinkText(using);
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SmartWebElement findElementByPartialLinkText(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByPartialLinkText(using);
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SimpleElementList findElementsByLinkText(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByLinkText(using);
		return new SimpleElementList( by, myInterface, elms );
	}

	@Override
	public SimpleElementList findElementsByPartialLinkText(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByLinkText(using);
		return new SimpleElementList( by, myInterface, elms );
	}

	@Override
	public SmartWebElement findElementByClassName(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByClassName(using);
		return new SimplePageElement( by, myInterface, elm );
	}

	@Override
	public SimpleElementList findElementsByClassName(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByClassName(using);
		return new SimpleElementList( by, myInterface, elms );
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
}
