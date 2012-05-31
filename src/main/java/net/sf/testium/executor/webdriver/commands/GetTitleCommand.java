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
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Executes the Selenium 2.0 getTitle command
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetTitleCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "getTitle";
	private static final String PAR_TITLE = "title";

    /**
	 * 
	 */
	public GetTitleCommand( WebInterface aWebInterface )
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

		ParameterVariable titlePar = (ParameterVariable) parameters.get(PAR_TITLE);
		String variableName = titlePar.getVariableName();

		String title = webDriver.getTitle();

		RunTimeVariable rtVariable = new RunTimeVariable( variableName, title );
		aVariables.add(rtVariable);

		result.setResult( VERDICT.PASSED );
		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the Variable Parameter
		Parameter titlePar = aParameters.get(PAR_TITLE);
		if ( titlePar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_TITLE + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}

		verifyParameterVariable(titlePar);

		return true;
	}

}
