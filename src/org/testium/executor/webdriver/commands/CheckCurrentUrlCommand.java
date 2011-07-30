/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan
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
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );
		RemoteWebDriver webDriver = this.getDriverAndSetResult(result);

		Parameter urlPar = parameters.get(PAR_URL);

		String currentUrl = webDriver.getCurrentUrl();
		setTestStepResult( null );

		if ( currentUrl.equals( urlPar.getValueAsString() ) )
		{
			result.setResult(TestResult.PASSED);
		}
		else
		{
			result.setResult(TestResult.FAILED);
			result.setComment( PAR_URL + " has value '" + currentUrl
			                   + "'. Expected '" + urlPar.getValueAsString() + "'" );
		}

		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters )
				   throws TestSuiteException
	{
		// Check the Value Parameter
		Parameter valuePar = aParameters.get(PAR_URL);
		if ( valuePar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_URL + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		if ( ! valuePar.getValueType().equals( String.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_URL + " must be a String",
			                              getInterfaceName() + "." + COMMAND );
		}

		if ( valuePar.getValueAsString().isEmpty() )
		{
			throw new TestSuiteException( PAR_URL + " cannot be empty",
			                              getInterfaceName() + "." + COMMAND );
		}

		return true;
	}

}
