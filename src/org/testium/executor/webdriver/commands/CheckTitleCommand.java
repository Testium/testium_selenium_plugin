/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.testium.configuration.SeleniumConfiguration;
import org.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 getTitle command and validates the result against a parameter
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckTitleCommand  extends WebDriverCommandExecutor
{
	private static final String COMMAND = "checkTitle";
	private static final String PAR_TITLE = "title";

    /**
	 * 
	 */
	public CheckTitleCommand( WebInterface aWebInterface )
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
		WebDriver webDriver = this.getDriverAndSetResult(result, browserType);

		String expectedTitle = "";
		Parameter titlePar = parameters.get(PAR_TITLE);
		if ( titlePar.getClass().equals( ParameterVariable.class ) )
		{
			expectedTitle = getVariableValueAs(String.class, titlePar, aVariables);
		}
		else if ( ParameterImpl.class.isInstance( titlePar ) )
		{
			expectedTitle = ((ParameterImpl) titlePar).getValueAsString();
		}
		else
		{
			throw new TestSuiteException( "parameter must be value or variable: " + titlePar.getName() );
		}

		String title = webDriver.getTitle();
		setTestStepResult( null, browserType );

		if ( title.equals( expectedTitle ) )
		{
			result.setResult(TestResult.PASSED);
		}
		else
		{
			result.setResult(TestResult.FAILED);
			result.setComment( PAR_TITLE + " has value '" + title
			                   + "'. Expected '" + expectedTitle + "'" );
		}

		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the Value Parameter
		Parameter valuePar = aParameters.get(PAR_TITLE);
		if ( valuePar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_TITLE + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		if ( valuePar.getClass().equals( ParameterVariable.class ) )
		{
			verifyParameterVariable(valuePar);
		}
		else
		{
			verifyParameterValue(valuePar, String.class);
		}

		return true;
	}

}
