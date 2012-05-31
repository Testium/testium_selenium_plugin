/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;

import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebDriver;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 forward command
 * 
 * @author Arjan Kranenburg
 *
 */
public class ForwardCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "forward";

    /**
	 * 
	 */
	public ForwardCommand( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface );
	}

	@Override
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		BROWSER_TYPE browserType = aVariables.getValueAs(BROWSER_TYPE.class, SeleniumConfiguration.BROWSERTYPE);

		TestStepResult result = new TestStepResult( aStep );
		WebDriver webDriver = this.getDriver(browserType);

		webDriver.navigate().forward();

		result.setResult( VERDICT.PASSED );
		return result;
	}


	@Override
	public boolean verifyParameters( ParameterArrayList aParameters )
	{
		return true;
	}

}
