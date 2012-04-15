package org.testium.executor.webdriver.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.openqa.selenium.WebDriver;
import org.testium.configuration.SeleniumConfiguration;
import org.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import org.testium.executor.TestStepCommandExecutor;
import org.testium.executor.general.SpecifiedParameter;
import org.testium.executor.webdriver.TestiumLogger;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

public abstract class GenericCommandExecutor implements TestStepCommandExecutor
{
	private String myCommand;
	private WebInterface myInterface;
	private ArrayList<SpecifiedParameter> myParameterSpecs;
	
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
	public GenericCommandExecutor( String command,
	                               WebInterface aWebInterface,
	                               ArrayList<SpecifiedParameter> parameterSpecs )
	{
		myCommand = command;
		myParameterSpecs = parameterSpecs;
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

	@Override
	public String getCommand()
	{
		return this.myCommand;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters)
				   throws TestSuiteException
	{
		Iterator<SpecifiedParameter> paramSpecItr = myParameterSpecs.iterator();
		
		while ( paramSpecItr.hasNext() )
		{
			SpecifiedParameter paramSpec = paramSpecItr.next();
			Parameter par = aParameters.get( paramSpec.getName() );

			// 1. Check that the parameter exists
			if ( par == null && ! paramSpec.isOptional() )
			{
				if ( paramSpec.isOptional() )
				{
					continue;
				} //else
				throw new TestSuiteException( "Mandatory parameter " + paramSpec.getName() + " is not set",
				                              myInterface.getInterfaceName() + "." + myCommand );
			}

			// 2. Check that the parameter is correctly a value or variable
			if ( par.getClass().equals( ParameterVariable.class ) )
			{
				verifyParameterVariable(par, paramSpec);
			}
			else // it's a value
			{
				verifyParameterValue(par, paramSpec);
			}
		}	
		
		return true;
	}

	/**
	 * @param aPar
	 * @param aParSpec
	 * @throws TestSuiteException
	 */
	protected void verifyParameterValue(Parameter aPar, SpecifiedParameter aParSpec) throws TestSuiteException
	{
		String parName = aParSpec.getName();
		if ( ! aParSpec.isValue() )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not allowed to be a value",
			                              myInterface.getInterfaceName() + "." + myCommand );
		}

		if ( ! ParameterImpl.class.isInstance( aPar ) )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not a value",
			                              myInterface.getInterfaceName() + "." + getCommand() );
		}

		ParameterImpl parameter = (ParameterImpl) aPar;
		Class<? extends Object> paramType = aParSpec.getClass();
		if ( ! parameter.getValueType().equals(paramType) )
		{
			throw new TestSuiteException( "Parameter " + parName + " must be a " + paramType.getSimpleName(),
			                              myInterface.getInterfaceName() + "." + getCommand() );
		}

		if ( paramType.equals(String.class) )
		{
			if ( ! aParSpec.isEmpty() && parameter.getValueAsString().isEmpty() )
			{
				throw new TestSuiteException( "Parameter " + parName + " cannot be empty",
				                              myInterface.getInterfaceName() + "." + getCommand() );
			}
		}
	}

	/**
	 * @param aPar
	 * @param aParSpec
	 * @throws TestSuiteException
	 */
	protected void verifyParameterVariable( Parameter aPar, SpecifiedParameter aParSpec ) throws TestSuiteException
	{
		String parName = aParSpec.getName();
		if ( ! aParSpec.isVariable() )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not allowed to be variable",
			                              myInterface.getInterfaceName() + "." + myCommand );
		}

		if ( ! ParameterVariable.class.isInstance(aPar) )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not defined as a variable",
			                              myInterface.getInterfaceName() + "." + getCommand() );
		}

		if ( ((ParameterVariable) aPar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + parName + " cannot be empty",
			                              myInterface.getInterfaceName() + "." + getCommand() );
		}
	}
}
