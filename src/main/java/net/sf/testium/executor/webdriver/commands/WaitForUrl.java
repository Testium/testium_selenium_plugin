package net.sf.testium.executor.webdriver.commands;
/**
 * 
 */
import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Command for waiting until the page has the expected URL
 * 
 * @author Arjan Kranenburg
 *
 */
public class WaitForUrl extends GenericSeleniumCommandExecutor
{
	private class isExpectedUrl implements ExpectedCondition<Boolean>
	{
		String expectedUrl;
		
		public isExpectedUrl( String url ) {
			this.expectedUrl = url;
		}

		public Boolean apply(WebDriver input) {

			String currentUrl = input.getCurrentUrl();

			return currentUrl.equalsIgnoreCase( expectedUrl );
		}
	}

	private static final String COMMAND = "waitForUrl";

	private static final String PAR_URL = "url";

	public static final SpecifiedParameter PARSPEC_URL = new SpecifiedParameter( 
			PAR_URL, String.class, false, true, true, false );

	private static final SpecifiedParameter PARSPEC_TIMEOUT = new SpecifiedParameter( 
			"timeout", Long.class, true, true, true, false )
			.setDefaultValue( 5L ); //seconds

	private static final SpecifiedParameter PARSPEC_SLEEPTIME = new SpecifiedParameter( 
			"sleeptime", Long.class, true, true, true, false )
			.setDefaultValue( 500L ); // milli-seconds

	public WaitForUrl( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_URL );
		this.addParamSpec( PARSPEC_TIMEOUT );
		this.addParamSpec( PARSPEC_SLEEPTIME );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		WebDriver webDriver = this.getDriver();

		String expectedUrl = (String) obtainValue( aVariables, parameters, PARSPEC_URL );
		Long timeout = obtainOptionalValue(aVariables, parameters, PARSPEC_TIMEOUT);
		Long sleeptime = obtainOptionalValue(aVariables, parameters, PARSPEC_SLEEPTIME);
		
		new WebDriverWait( webDriver, timeout, sleeptime )
			.until( new isExpectedUrl( expectedUrl ) );
	}
}
