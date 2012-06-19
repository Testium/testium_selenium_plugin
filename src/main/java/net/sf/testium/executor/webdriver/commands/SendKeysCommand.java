/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import net.sf.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 sendKeys command
 * 
 * @author Arjan Kranenburg
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
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );

		String keysToSend = "";
		Parameter keysPar = parameters.get(PAR_KEYS);
		if ( keysPar.getClass().equals( ParameterVariable.class ) )
		{
			keysToSend = getVariableValueAs(String.class, keysPar, aVariables);
		}
		else if ( ParameterImpl.class.isInstance( keysPar ) )
		{
			keysToSend = ((ParameterImpl) keysPar).getValueAsString();
		}
		else
		{
			throw new TestSuiteException( "parameter must be value or variable: " + keysPar.getName() );
		}

		Parameter elementPar = parameters.get(PAR_ELEMENT);
//		WebElement element = getVariableValueAs(WebElement.class, elementPar, aVariables);

//		Type valueOfType = null;
		ParameterVariable parVariable = (ParameterVariable) elementPar;
		String variableName = parVariable.getVariableName();
		WebElement element = aVariables.getValueAs( WebElement.class, variableName);
		if ( ! (element instanceof WebElement) )
		{
			throw new TestSuiteException( "Variable " + variableName + " is not of type " + WebElement.class.getSimpleName(),
			                              getInterfaceName() + "." + getCommand() );
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
		
		if ( keysPar.getClass().equals( ParameterVariable.class ) )
		{
			verifyParameterVariable(keysPar);
		}
		else
		{
			verifyParameterValue(keysPar, String.class);
		}

		// Check the Element Parameter Variable which is returned with the found web-element
		Parameter elementPar = aParameters.get(PAR_ELEMENT);
		if ( elementPar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_ELEMENT + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		verifyParameterVariable(elementPar);

		return true;
	}
}
