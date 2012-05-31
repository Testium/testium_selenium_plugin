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
 * Executes the Selenium 2.0 getCurrentUrl command
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetCurrentUrlCommand  extends WebDriverCommandExecutor
{
	private static final String COMMAND = "getCurrentUrl";
	private static final String PAR_URL = "url";

    /**
	 * 
	 */
	public GetCurrentUrlCommand( WebInterface aWebInterface )
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

		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_URL);
		String variableName = variablePar.getVariableName();

		String currentUrl = webDriver.getCurrentUrl();

		RunTimeVariable rtVariable = new RunTimeVariable( variableName, currentUrl );
		aVariables.add(rtVariable);

		result.setResult( VERDICT.PASSED );
		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the URL Parameter
		Parameter urlPar = aParameters.get(PAR_URL);
		if ( urlPar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_URL + " is not specified",
			                              getInterfaceName() + "." + COMMAND );
		}

		verifyParameterVariable(urlPar);

		return true;
	}

}
