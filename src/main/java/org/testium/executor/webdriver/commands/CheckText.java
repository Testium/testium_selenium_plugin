/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testium.executor.general.SpecifiedParameter;
import org.testium.executor.webdriver.WebInterface;
import org.testium.selenium.SmartWebElement;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Command for checking the text of a WebElement
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckText extends GenericSeleniumCommandExecutor {
	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			"element", SmartWebElement.class, false, false, true, false );

	private static final SpecifiedParameter PARSPEC_EXPECTED = new SpecifiedParameter( 
			"expected", String.class, false, true, true, false );

	private static final String COMMAND = "checkText";

	public CheckText( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_EXPECTED );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		WebElement element = (WebElement) obtainValue( aVariables, parameters, PARSPEC_ELEMENT );
		String expectedText = (String) obtainValue( aVariables, parameters, PARSPEC_EXPECTED );

		if ( element == null ) {
			if ( element instanceof SmartWebElement ) {
				SmartWebElement smartElm = (SmartWebElement) element;
				By by = smartElm.getBy();
	
				WebDriver driver = this.getDriver();
	
				element = driver.findElement(by);
				if ( element == null ) {
					throw new Exception( "Element cannot be found: " + by.toString() );
				}
			} else {
				throw new Exception( "Element was not found" );
			}
		}

		String actualText = element.getAttribute("value");
		if ( ! actualText.equals(expectedText) ) {
			throw new Exception( "Text is: \"" + actualText + "\" expected: \"" + expectedText + "\"" );
		}
	}
}
