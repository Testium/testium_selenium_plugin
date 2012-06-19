package net.sf.testium.executor.webdriver.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.executor.general.GenericCommandExecutor;
import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

public abstract class GenericSeleniumCommandExecutor extends GenericCommandExecutor
{
	
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
	}

	protected WebInterface getInterface()
	{
		return (WebInterface) super.getInterface();
	}

	protected WebDriver getDriver() {
		return this.getInterface().getDriver();
	}

	protected WebDriver getDriver( BROWSER_TYPE aBrowserType )
	{
		WebDriver webDriver = this.getInterface().getDriver( aBrowserType );

		return webDriver;
	}

	@Deprecated
	protected WebDriver getDriverAndSetResult( TestStepResult aTestStepResult, BROWSER_TYPE aBrowserType )
	{
		WebDriver webDriver = this.getInterface().getDriver( aBrowserType );

		return webDriver;
	}

	@Override
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );

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

	public boolean verifyParameters( ParameterArrayList aParameters)
			   throws TestSuiteException
	{
		Iterator<SpecifiedParameter> paramSpecItr = this.getParametersIterator();
		
		while ( paramSpecItr.hasNext() )
		{
			SpecifiedParameter paramSpec = paramSpecItr.next();
			Parameter par = aParameters.get( paramSpec.getName() );
	
			if ( ! this.verifyParameterExists( par, paramSpec ) )
			{
				continue; // Exception was thrown if it is mandatory
			}

			if ( paramSpec.getType().equals( By.class ) )
			{
				verifyBy(par, paramSpec);
			}
			else
			{
				this.verifyValueOrVariable(par, paramSpec);
			}
		}	
		
		return true;
	}

	protected void verifyBy( Parameter par, SpecifiedParameter paramSpec )
			throws TestSuiteException
	{
		if ( ! (par instanceof ParameterImpl) )
		{
			throw new TestSuiteException( "Parameter " + paramSpec.getName() + " is not a value",
                    toString() );
		}

		ParameterImpl valuePar = (ParameterImpl) par;

		if ( ! By.class.isAssignableFrom( valuePar.getValueType() ) )
		{
			throw new TestSuiteException( "Parameter " + paramSpec.getName() + " must be of type 'id', 'name'," +
			                              " 'linkText', 'partialLinkText', 'tagName', 'xPath'," +
			                              " 'className', or 'cssSelector'",
			                              toString() );
		}
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

		return element;
	}
}
