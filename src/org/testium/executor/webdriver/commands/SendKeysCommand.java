/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan
 *
 */
public class SendKeysCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "sendKeys";
	private static final String PAR_ELEMENT = "element";
	private static final String PAR_KEYS = "keys";

    /**
	 * 
	 */
	public SendKeysCommand( WebInterface aWebInterface )
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

		Parameter keysPar = parameters.get(PAR_KEYS);
		String keysToSend = keysPar.getValueAsString();

		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_ELEMENT);
		String variableName = variablePar.getVariableName();

		if ( ! aVariables.containsKey(variableName) )
		{
			throw new TestSuiteException( "Variable " + variableName + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}

		WebElement element = aVariables.getValueAs( WebElement.class, variableName);
		if ( element == null )
		{
			throw new TestSuiteException( "Variable " + variableName + " is not a WebElement",
			                              getInterfaceName() + "." + COMMAND );
		}

		try
		{
			element.sendKeys(keysToSend);
			result.setResult( VERDICT.PASSED );
		}
		catch( StaleElementReferenceException sere )
		{
			result.setResult( VERDICT.FAILED );
			result.addComment( sere.getLocalizedMessage() );
		}

		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the KeysToSend Parameter
		Parameter keysPar = aParameters.get(PAR_KEYS);
		if ( keysPar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_KEYS + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		if ( ! keysPar.getValueType().equals( String.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_KEYS + " must be a String",
			                              getInterfaceName() + "." + COMMAND );
		}

		if ( keysPar.getValueAsString().isEmpty() )
		{
			throw new TestSuiteException( PAR_KEYS + " cannot be empty",
			                              getInterfaceName() + "." + COMMAND );
		}

		// Check the Element Parameter Variable which is returned with the found web-element
		Parameter elementPar = aParameters.get(PAR_ELEMENT);
		if ( elementPar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_ELEMENT + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		if ( ! elementPar.getClass().equals( ParameterVariable.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_ELEMENT + " is not defined as a variable",
			                              getInterfaceName() + "." + COMMAND );
		}

		if ( ((ParameterVariable) elementPar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + PAR_ELEMENT + " cannot be empty",
			                              getInterfaceName() + "." + COMMAND );
		}

		return true;
	}

}
