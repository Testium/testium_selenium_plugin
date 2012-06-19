/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;
import net.sf.testium.selenium.SmartWebDriverWait;
import net.sf.testium.selenium.SmartWebElement;

import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Command for checking the text of a WebElement
 * 
 * @author Arjan Kranenburg
 *
 */
public class WaitFor extends GenericSeleniumCommandExecutor
{
	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			"element", SmartWebElement.class, false, false, true, false );

	private static final SpecifiedParameter PARSPEC_PRESENT = new SpecifiedParameter( 
			"present", Boolean.class, true, true, false, false )
			.setDefaultValue( true );

	private static final SpecifiedParameter PARSPEC_TIMEOUT = new SpecifiedParameter( 
			"timeout", Integer.class, true, true, true, false )
			.setDefaultValue( 5L ); //seconds

	private static final SpecifiedParameter PARSPEC_SLEEPTIME = new SpecifiedParameter( 
			"sleeptime", Integer.class, true, true, true, false )
			.setDefaultValue( 500L ); // milli-seconds

	private static final String COMMAND = "waitFor";

	public WaitFor( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_PRESENT );
		this.addParamSpec( PARSPEC_TIMEOUT );
		this.addParamSpec( PARSPEC_SLEEPTIME );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		WebElement element = obtainElement(aVariables, parameters, PARSPEC_ELEMENT);
		if( ! (element instanceof SmartWebElement) )
		{
			throw new Exception( "Mandatory element is not a SmartWebElement" );
		}
		SmartWebElement smartElement = (SmartWebElement) element;
		
		boolean presentFlag = obtainOptionalValue(aVariables, parameters, PARSPEC_PRESENT);
		Long timeout = obtainOptionalValue(aVariables, parameters, PARSPEC_TIMEOUT);
		Long sleeptime = obtainOptionalValue(aVariables, parameters, PARSPEC_SLEEPTIME);
		
		new SmartWebDriverWait( getDriver(), timeout, sleeptime )
			.until(smartElement.getBy(), presentFlag);
	}
}
