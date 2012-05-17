/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.WebDriver;
import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.executor.webdriver.WebInterface;
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
 * Executes the Selenium 2.0 getCurrentUrl command and validates the result against a parameter
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckCurrentUrlCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "checkCurrentUrl";
	private static final String PAR_URL = "url";

    /**
	 * 
	 */
	public CheckCurrentUrlCommand( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface) ;
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

		String expectedUrl = "";
		Parameter urlPar = parameters.get(PAR_URL);
		if ( ParameterVariable.class.isInstance( urlPar ) )
		{
			expectedUrl = getVariableValueAs(String.class, urlPar, aVariables);
		}
		else if ( ParameterImpl.class.isInstance( urlPar ) )
		{
			expectedUrl = ((ParameterImpl) urlPar).getValueAsString();
		}
		else
		{
			throw new TestSuiteException( "parameter must be value or variable: " + urlPar.getName() );
		}

		String currentUrl = webDriver.getCurrentUrl();
		setTestStepResult( null, browserType );

		if ( currentUrl.equals( expectedUrl ) )
		{
			result.setResult(TestResult.PASSED);
		}
		else
		{
			result.setResult(TestResult.FAILED);
			result.setComment( PAR_URL + " has value '" + currentUrl
			                   + "'. Expected '" + expectedUrl + "'" );
		}

		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters )
				   throws TestSuiteException
	{
		// Check the Value Parameter
		Parameter urlPar = aParameters.get(PAR_URL);
		if ( urlPar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_URL + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		if ( urlPar.getClass().equals( ParameterVariable.class ) )
		{
			verifyParameterVariable(urlPar);
		}
		else
		{
			verifyParameterValue(urlPar, String.class);
		}

		return true;
	}

}
