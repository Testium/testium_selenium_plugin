/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.utils.RunTimeData;

/**
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
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );
		RemoteWebDriver webDriver = this.getDriverAndSetResult(result);

		Parameter urlPar = parameters.get(PAR_URL);
		String url = urlPar.getValueAsString();

		webDriver.get(url);
		setTestStepResult( null );

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
		
		if ( ! urlPar.getValueType().equals( String.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_URL + " must be a String",
			                              getInterfaceName() + "." + COMMAND );
		}

		if ( urlPar.getValueAsString().isEmpty() )
		{
			throw new TestSuiteException( PAR_URL + " cannot be empty",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		return true;
	}

}
