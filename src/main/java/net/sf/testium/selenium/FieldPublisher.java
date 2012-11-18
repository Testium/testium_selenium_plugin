package net.sf.testium.selenium;

import java.util.List;

import org.openqa.selenium.WebElement;


/**
 * Maps Testium Variable names to WebElements.
 * 
 * @author akranenburg
 *
 */
public interface FieldPublisher {

	/**
	 * Adds an element to the map
	 * @param anElement
	 */
	public void addElement( String varName, WebElement element );
	
	/**
	 * Adds an element-list to the map
	 * @param anElementList
	 */
	public void addElement( String varName, List<WebElement> elements );
	
	/**
	 * @param name
	 * @return the element from the map
	 */
	public WebElement getElement( String varName );
}
