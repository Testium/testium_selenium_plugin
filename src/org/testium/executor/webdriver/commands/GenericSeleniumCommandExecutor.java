package org.testium.executor.webdriver.commands;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testium.configuration.SeleniumConfiguration;
import org.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import org.testium.executor.general.GenericCommandExecutor;
import org.testium.executor.general.SpecifiedParameter;
import org.testium.executor.webdriver.TestiumLogger;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

public abstract class GenericSeleniumCommandExecutor extends GenericCommandExecutor
{
	private WebInterface myInterface;
	
	/**
	 * @param aVariables
	 * @param parameters
	 * @param result
	 * @throws TestSuiteException
	 */
	abstract protected void doExecute( RunTimeData aVariables,
	                                   ParameterArrayList parameters,
	                                   TestStepResult result) throws Exception;

	/**
	 * @param command
	 * @param parameterSpecs
	 */
	public GenericSeleniumCommandExecutor( String command,
	                               WebInterface aWebInterface,
	                               ArrayList<SpecifiedParameter> parameterSpecs )
	{
		super( command, aWebInterface, parameterSpecs );
		myInterface = aWebInterface;
	}

	protected WebDriver getDriverAndSetResult( TestStepResult aTestStepResult, BROWSER_TYPE aBrowserType )
	{
		WebDriver webDriver = myInterface.getDriver( aBrowserType );

		setTestStepResult( aTestStepResult, aBrowserType );
		
		return webDriver;
	}

	/**
	 * @param aTestStepResult
	 */
	protected void setTestStepResult( TestStepResult aTestStepResult, BROWSER_TYPE aBrowserType )
	{
		WebDriver webDriver = myInterface.getDriver( aBrowserType );
		if( webDriver.getClass().isAssignableFrom(TestiumLogger.class) )
		{
			TestiumLogger logger = (TestiumLogger) webDriver;
			logger.setTestStepResult(aTestStepResult);
		}
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
		this.setTestStepResult(result, browserType);

		try
		{
			doExecute(aVariables, parameters, result);
			result.setResult( VERDICT.PASSED );
		}
		catch (Exception e)
		{
			result.setResult( VERDICT.FAILED );
			result.addComment( e.getMessage() );
		}

		return result;
	}
}
