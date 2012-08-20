/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebDriver;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 getCurrentUrl command and validates the result against a parameter
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckCurrentUrl extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "checkCurrentUrl";
	private static final String PAR_URL = "url";

	public static final SpecifiedParameter PARSPEC_URL = new SpecifiedParameter( 
			PAR_URL, String.class, false, true, true, false );


    /**
	 * 
	 */
	public CheckCurrentUrl( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_URL );
	}

	@Override
	protected void doExecute( RunTimeData aVariables,
							  ParameterArrayList parameters,
							  TestStepResult result )
			throws Exception {
		WebDriver webDriver = this.getDriver();

		String expectedUrl = (String) obtainValue( aVariables, parameters, PARSPEC_URL );

		String currentUrl = webDriver.getCurrentUrl();

		if ( ! currentUrl.equalsIgnoreCase( expectedUrl ) )
		{
			throw new Exception( PAR_URL + " has value '" + currentUrl
	                   + "'. Expected '" + expectedUrl + "' (ignoring case)" );
		}
	}
}
