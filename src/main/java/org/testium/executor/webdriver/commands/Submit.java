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
public class Submit extends GenericSeleniumCommandExecutor
{
	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			"element", SmartWebElement.class, false, false, true, false );

	private static final String COMMAND = "submit";

	public Submit( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		WebElement element = obtainElement(aVariables, parameters, PARSPEC_ELEMENT);
		if( element == null )
		{
			throw new Exception( "Mandatory element was null" );
		}

		element.submit();
//		String actualText = element.getAttribute("value");
//		if ( ! actualText.equals(expectedText) ) {
//			throw new Exception( "Text is: \"" + actualText + "\" expected: \"" + expectedText + "\"" );
//		}
	}
}
