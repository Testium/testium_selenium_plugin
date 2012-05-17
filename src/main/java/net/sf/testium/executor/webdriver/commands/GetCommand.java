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
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 get command
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "get";
	private static final String PAR_URL = "url";

    /**
	 * 
	 */
	public GetCommand( WebInterface aWebInterface )
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

		String url = "";
		Parameter urlPar = parameters.get(PAR_URL);
		if ( urlPar.getClass().equals( ParameterVariable.class ) )
		{
			url = getVariableValueAs(String.class, urlPar, aVariables);
		}
		else if ( ParameterImpl.class.isInstance( urlPar ) )
		{
			url = ((ParameterImpl) urlPar).getValueAsString();
		}
		else
		{
			throw new TestSuiteException( "parameter must be value or variable: " + urlPar.getName() );
		}

		webDriver.get(url);
		setTestStepResult( null, browserType );

		result.setResult( VERDICT.PASSED );
		return result;
	}


	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		Parameter urlPar = aParameters.get(PAR_URL);
		if ( urlPar == null )
		{
			throw new TestSuiteException( PAR_URL + " is not specified",
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
