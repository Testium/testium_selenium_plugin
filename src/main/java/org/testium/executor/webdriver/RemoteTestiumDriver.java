package org.testium.executor.webdriver;

import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testium.selenium.SimplePageElement;
import org.testium.selenium.SmartElementList;

public class RemoteTestiumDriver extends TestiumDriver implements JavascriptExecutor,
															  FindsById,
															  FindsByClassName,
															  FindsByLinkText,
															  FindsByName,
															  FindsByCssSelector,
															  FindsByTagName,
															  FindsByXPath,
															  HasInputDevices,
															  HasCapabilities {

	private final RemoteWebDriver driver;
	
	public RemoteTestiumDriver( RemoteWebDriver aDriver, WebInterface anInterface ) {
		super( aDriver, anInterface );
		driver = aDriver;
	}


	public Capabilities getCapabilities() {
		return driver.getCapabilities();
	}

	public Keyboard getKeyboard() {
		return driver.getKeyboard();
	}

	public Mouse getMouse() {
		return driver.getMouse();
	}

	public Object executeScript(String script, Object... args) {
		return driver.executeScript(script, args);
	}

	public Object executeAsyncScript(String script, Object... args) {
		return driver.executeAsyncScript(script, args);
	}

	public WebElement findElementById(String using) {
		By by = By.id(using);
		WebElement elm = driver.findElementById(using);
		return new SimplePageElement(elm, by);
	}

	public List<WebElement> findElementsById(String using) {
		By by = By.id(using);
		List<WebElement> elms = driver.findElementsById(using);
		return new SmartElementList( by, elms );
	}

	public WebElement findElementByXPath(String using) {
		By by = By.id(using);
		WebElement elm = driver.findElementByXPath(using);
		return new SimplePageElement(elm, by);
	}

	public List<WebElement> findElementsByXPath(String using) {
		By by = By.id(using);
		List<WebElement> elms = driver.findElementsByXPath(using);
		return new SmartElementList( by, elms );
	}

	public WebElement findElementByTagName(String using) {
		By by = By.id(using);
		WebElement elm = driver.findElementByTagName(using);
		return new SimplePageElement(elm, by);
	}

	public List<WebElement> findElementsByTagName(String using) {
		By by = By.id(using);
		List<WebElement> elms = driver.findElementsByTagName(using);
		return new SmartElementList( by, elms );
	}

	public WebElement findElementByCssSelector(String using) {
		By by = By.id(using);
		WebElement elm = driver.findElementByCssSelector(using);
		return new SimplePageElement(elm, by);
	}

	public List<WebElement> findElementsByCssSelector(String using) {
		By by = By.id(using);
		List<WebElement> elms = driver.findElementsByCssSelector(using);
		return new SmartElementList( by, elms );
	}

	public WebElement findElementByName(String using) {
		By by = By.id(using);
		WebElement elm = driver.findElementByName(using);
		return new SimplePageElement(elm, by);
	}

	public List<WebElement> findElementsByName(String using) {
		By by = By.id(using);
		List<WebElement> elms = driver.findElementsByName(using);
		return new SmartElementList( by, elms );
	}

	public WebElement findElementByLinkText(String using) {
		By by = By.id(using);
		WebElement elm = driver.findElementByLinkText(using);
		return new SimplePageElement(elm, by);
	}

	public WebElement findElementByPartialLinkText(String using) {
		By by = By.id(using);
		WebElement elm = driver.findElementByPartialLinkText(using);
		return new SimplePageElement(elm, by);
	}

	public List<WebElement> findElementsByLinkText(String using) {
		By by = By.id(using);
		List<WebElement> elms = driver.findElementsByLinkText(using);
		return new SmartElementList( by, elms );
	}

	public List<WebElement> findElementsByPartialLinkText(String using) {
		By by = By.id(using);
		List<WebElement> elms = driver.findElementsByLinkText(using);
		return new SmartElementList( by, elms );
	}

	public WebElement findElementByClassName(String using) {
		By by = By.id(using);
		WebElement elm = driver.findElementByClassName(using);
		return new SimplePageElement(elm, by);
	}

	public List<WebElement> findElementsByClassName(String using) {
		By by = By.id(using);
		List<WebElement> elms = driver.findElementsByClassName(using);
		return new SmartElementList( by, elms );
	}
}
