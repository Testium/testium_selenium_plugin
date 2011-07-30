/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testium.executor.TestStepCommandExecutor;
import org.testium.executor.webdriver.TestiumLogger;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan Kranenburg
 *
 */
public abstract class WebDriverCommandExecutor implements TestStepCommandExecutor
{
	private String myCommand;
	private WebInterface myInterface;

	@Override
	abstract public TestStepResult execute( TestStepSimple aStep,
	                                        RunTimeData aVariables,
	                                        File aLogDir )
					throws TestSuiteException;

	@Override
	abstract public boolean verifyParameters( ParameterArrayList aParameters )
					throws TestSuiteException;

    /**
	 * 
	 */
	public WebDriverCommandExecutor( String aCommand, WebInterface aWebInterface )
	{
		myCommand = aCommand;
		myInterface = aWebInterface;
	}

	protected WebInterface getInterface()
	{
		return myInterface;
	}

	@Override
	public String getCommand()
	{
		return myCommand;
	}

	public String getInterfaceName()
	{
		return myInterface.getInterfaceName();
	}

	protected RemoteWebDriver getDriverAndSetResult( TestStepResult aTestStepResult )
	{
		RemoteWebDriver webDriver = myInterface.getDriver();

		setTestStepResult( aTestStepResult );
		
		return webDriver;
	}

	/**
	 * @param aRemoteWebDriver
	 * @param aTestStepResult
	 */
	protected void setTestStepResult( TestStepResult aTestStepResult )
	{
		RemoteWebDriver webDriver = myInterface.getDriver();
		if( webDriver.getClass().isAssignableFrom(TestiumLogger.class) )
		{
			TestiumLogger logger = (TestiumLogger) webDriver;
			logger.setTestStepResult(aTestStepResult);
		}
	}
}
