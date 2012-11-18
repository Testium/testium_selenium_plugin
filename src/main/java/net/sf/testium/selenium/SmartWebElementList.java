package net.sf.testium.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface SmartWebElementList extends List<WebElement>{

	/**
	 * @return the original {@link By} used to find the element
	 */
	public By getBy();
}
