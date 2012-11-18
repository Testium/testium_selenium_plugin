package net.sf.testium.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface SmartWebElement extends WebElement {

	/**
	 * @return the stored element<br>
	 * Note: It is not validated if the element is still present in the DOM.<br>
	 * For that use {@link org.openqa.selenium.WebElement#findElement(By)}
	 */
	public WebElement getElement();
	
	/**
	 * @return the original {@link By} used to find the element
	 */
	public By getBy();
}
