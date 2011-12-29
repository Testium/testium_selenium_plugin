/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.testium.executor.TestStepCommandExecutor;
import org.testium.executor.webdriver.TestiumLogger;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan Kranenburg
 *
 */
public abstract class WebDriverCommandExecutor implements TestStepCommandExecutor
{
	public static enum ALLOWED_PAR_TYPE
	{
		VALUE,
		VARIABLE,
		VAL_OR_VAR,
		SUBPARAMETER,
	}

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

	protected WebDriver getDriverAndSetResult( TestStepResult aTestStepResult )
	{
		WebDriver webDriver = myInterface.getDriver();

		setTestStepResult( aTestStepResult );
		
		return webDriver;
	}

	/**
	 * @param aRemoteWebDriver
	 * @param aTestStepResult
	 */
	protected void setTestStepResult( TestStepResult aTestStepResult )
	{
		WebDriver webDriver = myInterface.getDriver();
		if( webDriver.getClass().isAssignableFrom(TestiumLogger.class) )
		{
			TestiumLogger logger = (TestiumLogger) webDriver;
			logger.setTestStepResult(aTestStepResult);
		}
	}


	/**
	 * @param aPar
	 * @param aType
	 * @throws TestSuiteException
	 */
	protected void verifyParameterValue(Parameter aPar, Class<? extends Object> aType) throws TestSuiteException
	{
		if ( aPar == null )
		{
			throw new TestSuiteException( "Parameter is not set",
			                              getInterfaceName() + "." + getCommand() );
		}

		String parName = aPar.getName();
		if ( ! ParameterImpl.class.isInstance( aPar ) )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not a value",
			                              getInterfaceName() + "." + getCommand() );
		}

		ParameterImpl parameter = (ParameterImpl) aPar;
		if ( ! parameter.getValueType().equals( aType ) )
		{
			throw new TestSuiteException( "Parameter " + parName + " must be a " + aType.getSimpleName(),
			                              getInterfaceName() + "." + getCommand() );
		}

		if ( aType == String.class )
		{
			if ( parameter.getValueAsString().isEmpty() )
			{
				throw new TestSuiteException( parName + " cannot be empty",
				                              getInterfaceName() + "." + getCommand() );
			}
		}
	}

	/**
	 * @param elementPar
	 * @throws TestSuiteException
	 */
	protected void verifyParameterVariable( Parameter aPar ) throws TestSuiteException
	{
		if ( aPar == null )
		{
			throw new TestSuiteException( "Parameter is not set",
			                              getInterfaceName() + "." + getCommand() );
		}
		
		String parName = aPar.getName();
		if ( ! ParameterVariable.class.isInstance(aPar) )
		{
			throw new TestSuiteException( "Parameter " + parName + " is not defined as a variable",
			                              getInterfaceName() + "." + getCommand() );
		}

		if ( ((ParameterVariable) aPar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + parName + " cannot be empty",
			                              getInterfaceName() + "." + getCommand() );
		}
	}

	/**
	 * @param aVariables
	 * @param aPar
	 * @return
	 * @throws TestSuiteException
	 */
	protected <Type> Type getVariableValueAs( Class<Type> aType, Parameter aPar, RunTimeData aVariables )
																				throws TestSuiteException
	{
		Type valueOfType = null;
		ParameterVariable parVariable = (ParameterVariable) aPar;
		String variableName = parVariable.getVariableName();

		if ( ! aVariables.containsKey(variableName) )
		{
			throw new TestSuiteException( "Variable " + variableName + " is not set",
			                              getInterfaceName() + "." + getCommand() );
		}

		valueOfType = aVariables.getValueAs( aType, variableName);
		if ( valueOfType == null )
		{
			throw new TestSuiteException( "Variable " + variableName + " is not of type " + aType.getSimpleName(),
			                              getInterfaceName() + "." + getCommand() );
		}
		return valueOfType;
	}
}
