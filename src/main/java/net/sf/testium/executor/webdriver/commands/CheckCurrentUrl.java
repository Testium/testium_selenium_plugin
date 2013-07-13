/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebDriver;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
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
	private static final String PAR_MATCH = "match";

	public static final SpecifiedParameter PARSPEC_URL = new SpecifiedParameter( 
			PAR_URL, String.class, false, true, true, false );
	public static final SpecifiedParameter PARSPEC_MATCH = new SpecifiedParameter( 
			PAR_MATCH, String.class, true, true, true, false )
				.setDefaultValue("exact");


    /**
	 * 
	 */
	public CheckCurrentUrl( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_URL );
		this.addParamSpec( PARSPEC_MATCH );
	}

	@Override
	protected void doExecute( RunTimeData aVariables,
							  ParameterArrayList parameters,
							  TestStepCommandResult result )
			throws Exception {
		WebDriver webDriver = this.getDriver();

		String expectedUrl = (String) obtainValue( aVariables, parameters, PARSPEC_URL );
		String match = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_MATCH);

		String currentUrl = webDriver.getCurrentUrl();

		if ( match.equalsIgnoreCase( "exact" ) )
		{
			checkExact(expectedUrl, currentUrl,
					"Actual URL: \"" + currentUrl + "\" is not equal to: \"" + expectedUrl + "\"");
			return;
		}
		else if ( match.equalsIgnoreCase( "contains" ) )
		{
			checkContains(expectedUrl, currentUrl,
					"Actual URL: \"" + currentUrl + "\" does not contain: \"" + expectedUrl + "\"");
			return;
		}
		else if ( match.equalsIgnoreCase( "startsWith" ) )
		{
			checkStartsWith(expectedUrl, currentUrl,
					"Actual URL: \"" + currentUrl + "\" does not start with: \"" + expectedUrl + "\"" );
			return;
		}
		else if ( match.equalsIgnoreCase( "endsWith" ) )
		{
			checkEndsWith(expectedUrl, currentUrl,
					"Actual URL: \"" + currentUrl + "\" does not end with: \"" + expectedUrl + "\"" );
			return;
		}
		else
		{
			throw new Exception( "match criteria \"" + match + "\" is not supported. Only exact, contains, startsWith, or endsWith" );
		}
	}

	private void checkExact(String expectedUrl, String currentUrl,
			String message) throws Exception {
		if ( ! currentUrl.equalsIgnoreCase( expectedUrl ) )
		{
			throw new Exception( message );
		}
	}

	private void checkContains(String expectedUrl, String currentUrl,
			String message) throws Exception {
		if ( ! currentUrl.contains(expectedUrl) ) {
			throw new Exception( message );
		}
	}

	private void checkStartsWith(String expectedUrl, String currentUrl,
			String message) throws Exception {
		if ( ! currentUrl.startsWith(expectedUrl) ) {
			throw new Exception( message );
		}
	}

	private void checkEndsWith(String expectedUrl, String currentUrl,
			String message) throws Exception {
		if ( ! currentUrl.endsWith(expectedUrl) ) {
			throw new Exception( message );
		}
	}
}
