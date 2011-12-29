/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
 * @author Arjan Kranenburg
 *
 */
public class FindElementCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "findElement";
	private static final String PAR_BY = "by";
	private static final String PAR_ELEMENT = "element";

    /**
	 * 
	 */
	public FindElementCommand( WebInterface aWebInterface )
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

		Parameter byPar = parameters.get(PAR_BY);
		By by = byPar.getValueAs(By.class);

		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_ELEMENT);
		String variableName = variablePar.getVariableName();

		WebElement element = webDriver.findElement(by);
		setTestStepResult( null );

		RunTimeVariable rtVariable = new RunTimeVariable( variableName, element );
		aVariables.add(rtVariable);

		result.setResult( VERDICT.PASSED );

		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the Value Parameter
		Parameter valuePar = aParameters.get(PAR_BY);
		if ( valuePar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_BY + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		if ( ! valuePar.getValueType().toString().startsWith( By.class.toString() ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_BY + " must be of type 'id', 'name'," +
			                              " 'linkText', 'partialLinkText', 'tagName', 'xPath'," +
			                              " 'className', or 'cssSelector'",
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
