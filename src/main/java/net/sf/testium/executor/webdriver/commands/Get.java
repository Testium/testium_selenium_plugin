/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;
import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;

import org.openqa.selenium.WebDriver;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 get command
 * 
 * @author Arjan Kranenburg
 *
 */
public class Get extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "get";
	private static final String PAR_URL = "url";
	private static final String PAR_RELATIVE = "relative";
	private static final String PAR_BROWSER = "browser";

	public static final SpecifiedParameter PARSPEC_URL = new SpecifiedParameter( 
			PAR_URL, String.class, false, true, true, false );

	public static final SpecifiedParameter PARSPEC_RELATIVE = new SpecifiedParameter( 
			PAR_RELATIVE, Boolean.class, true, true, false, false )
		.setDefaultValue( false );

	public static final SpecifiedParameter PARSPEC_BROWSER = new SpecifiedParameter( 
			PAR_BROWSER, String.class, true, true, true, false )
		.setDefaultValue("");

	/**
	 * 
	 */
	public Get( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_URL );
		this.addParamSpec( PARSPEC_RELATIVE );
		this.addParamSpec( PARSPEC_BROWSER );
	}

	@Override
	protected void doExecute( RunTimeData aVariables,
							  ParameterArrayList parameters,
							  TestStepResult result )
			throws Exception {

		String url = (String) obtainValue( aVariables, parameters, PARSPEC_URL );
		Boolean relative = (Boolean) this.obtainOptionalValue( aVariables, parameters, PARSPEC_RELATIVE );
		if ( relative )
		{
			String baseUrl = this.getInterface().getBaseUrl();
			url = baseUrl + url;
		}

		BROWSER_TYPE type = aVariables.getValueAs(BROWSER_TYPE.class, SeleniumConfiguration.BROWSERTYPE);

		String browserType = (String) this.obtainOptionalValue( aVariables, parameters, PARSPEC_BROWSER );
		if ( ! browserType.isEmpty() )
		{
			type = BROWSER_TYPE.enumOf(browserType);
		}

		WebDriver webDriver = this.getDriver( type );
		webDriver.get(url);
	}

}
