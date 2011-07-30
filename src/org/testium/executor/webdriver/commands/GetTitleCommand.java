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
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * @author Arjan
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
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );
		RemoteWebDriver webDriver = this.getDriverAndSetResult(result);

		ParameterVariable titlePar = (ParameterVariable) parameters.get(PAR_TITLE);
		String variableName = titlePar.getVariableName();

		String title = webDriver.getTitle();
		setTestStepResult( null );

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

		if ( ! titlePar.getClass().equals( ParameterVariable.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_TITLE + " is not defined as a variable",
			                              getInterfaceName() + "." + COMMAND );
		}

		if ( ((ParameterVariable) titlePar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + PAR_TITLE + " cannot be empty",
			                              getInterfaceName() + "." + COMMAND );
		}

		return true;
	}

}
