package org.testium.executor.webdriver.commands;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testium.configuration.SeleniumConfiguration;
import org.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import org.testium.executor.general.GenericCommandExecutor;
import org.testium.executor.general.SpecifiedParameter;
import org.testium.executor.webdriver.TestiumLogger;
import org.testium.executor.webdriver.WebInterface;
import org.testium.selenium.SmartWebElement;
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

	protected WebDriver getDriver() {
		return myInterface.getDriver();
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
	
	/**
	 * @param aVariables
	 * @param parameters
	 * @param aParSpec
	 * 
	 * @return the value as a WebElement (can be null, if optional).
	 * If the element is a SmartWebElement the element is reloaded.
	 * @throws Exception when not an element or when mandatory parameter is not found
	 */
	protected WebElement obtainElement( RunTimeData aVariables,
									  ParameterArrayList parameters,
									  SpecifiedParameter paramSpec ) throws Exception
	{
		WebElement element = (WebElement) super.obtainValue( aVariables, parameters, paramSpec );

		if ( element == null )
		{
			if ( paramSpec.isOptional() )
			{
				return null;
			}
			throw new Exception( "Mandatory element was not found or was not a WebElement: " + paramSpec.getName() );
		}
		// element != null
		
		if ( ! (element instanceof SmartWebElement) )
		{
			return element;
		}
		// SmartWebElement
		
		SmartWebElement smartElm = (SmartWebElement) element;
		By by = smartElm.getBy();
		element = this.getDriver().findElement(by);
		if ( element == null && ! paramSpec.isOptional() )
		{
			throw new Exception( "Mandatory element could not be found: " + by.toString() );
		}

		return element;
	}
}
