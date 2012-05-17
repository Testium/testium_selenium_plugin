/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.WebDriver;
import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 back command
 * 
 * @author Arjan Kranenburg
 *
 */
public class BackCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "back";

    /**
	 * 
	 */
	public BackCommand( WebInterface aWebInterface )
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

		TestStepResult result = new TestStepResult( aStep );
		BROWSER_TYPE browserType = aVariables.getValueAs(BROWSER_TYPE.class, SeleniumConfiguration.BROWSERTYPE);
		WebDriver webDriver = this.getDriverAndSetResult(result, browserType);

		webDriver.navigate().back();
		setTestStepResult( null, browserType );

		result.setResult( VERDICT.PASSED );
		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters )
	{
		return true;
	}

}
