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
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );
		RemoteWebDriver webDriver = this.getDriverAndSetResult(result);

		Parameter titlePar = parameters.get(PAR_TITLE);

		String title = webDriver.getTitle();
		setTestStepResult( null );

		if ( title.equals( titlePar.getValueAsString() ) )
		{
			result.setResult(TestResult.PASSED);
		}
		else
		{
			result.setResult(TestResult.FAILED);
			result.setComment( PAR_TITLE + " has value '" + title
			                   + "'. Expected '" + titlePar.getValueAsString() + "'" );
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
		
		if ( ! valuePar.getValueType().equals( String.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_TITLE + " must be a String",
			                              getInterfaceName() + "." + COMMAND );
		}

		if ( valuePar.getValueAsString().isEmpty() )
		{
			throw new TestSuiteException( PAR_TITLE + " cannot be empty",
			                              getInterfaceName() + "." + COMMAND );
		}

		return true;
	}

}
