package net.sf.testium.executor.webdriver;

import java.util.List;

import net.sf.testium.selenium.SimplePageElement;
import net.sf.testium.selenium.SmartElementList;
import net.sf.testium.selenium.SmartWebElement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.iphone.IPhoneDriver;

public class TestiumIPhoneDriver extends IPhoneDriver {

	public TestiumIPhoneDriver() throws Exception {
		super();
	}

	@Override
	public SmartElementList findElements(By by) {
		List<WebElement> elms = by.findElements(this);
		if ( elms instanceof SmartElementList ) {
			return (SmartElementList) elms;
		}
		return new SmartElementList(elms, by);
	}

	@Override
	public SmartWebElement findElement(By by) {
		WebElement elm = by.findElement(this);
		if ( elm instanceof SmartWebElement ) {
			return (SmartWebElement) elm;
		}
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartWebElement findElementById(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementById(using);
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartElementList findElementsById(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsById(using);
		return new SmartElementList( elms, by );
	}

	@Override
	public SmartWebElement findElementByXPath(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByXPath(using);
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartElementList findElementsByXPath(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByXPath(using);
		return new SmartElementList( elms, by );
	}

	@Override
	public SmartWebElement findElementByTagName(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByTagName(using);
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartElementList findElementsByTagName(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByTagName(using);
		return new SmartElementList( elms, by );
	}

	@Override
	public SmartWebElement findElementByCssSelector(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByCssSelector(using);
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartElementList findElementsByCssSelector(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByCssSelector(using);
		return new SmartElementList( elms, by );
	}

	@Override
	public SmartWebElement findElementByName(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByName(using);
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartElementList findElementsByName(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByName(using);
		return new SmartElementList( elms, by );
	}

	@Override
	public SmartWebElement findElementByLinkText(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByLinkText(using);
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartWebElement findElementByPartialLinkText(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByPartialLinkText(using);
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartElementList findElementsByLinkText(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByLinkText(using);
		return new SmartElementList( elms, by );
	}

	@Override
	public SmartElementList findElementsByPartialLinkText(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByLinkText(using);
		return new SmartElementList( elms, by );
	}

	@Override
	public SmartWebElement findElementByClassName(String using) {
		By by = By.id(using);
		WebElement elm = super.findElementByClassName(using);
		return new SimplePageElement(elm, by);
	}

	@Override
	public SmartElementList findElementsByClassName(String using) {
		By by = By.id(using);
		List<WebElement> elms = super.findElementsByClassName(using);
		return new SmartElementList( elms, by );
	}
}
