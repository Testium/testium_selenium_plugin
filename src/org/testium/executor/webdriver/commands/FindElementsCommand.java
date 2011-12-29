/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;
import java.util.List;

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
public class FindElementsCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "findElements";
	private static final String PAR_BY = "by";
	private static final String PAR_ELEMENTS = "elements";


    /**
	 * 
	 */
	public FindElementsCommand( WebInterface aWebInterface )
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

		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_ELEMENTS);
		String variableName = variablePar.getVariableName();

		List<WebElement> elements = webDriver.findElements(by);
		setTestStepResult( null );

		RunTimeVariable rtVariable = new RunTimeVariable( variableName, elements );
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
		Parameter elementsPar = aParameters.get(PAR_ELEMENTS);
		if ( elementsPar == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_ELEMENTS + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}
		
		if ( ! elementsPar.getClass().equals( ParameterVariable.class ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_ELEMENTS + " is not defined as a variable",
			                              getInterfaceName() + "." + COMMAND );
		}

		if ( ((ParameterVariable) elementsPar).getVariableName().isEmpty() )
		{
			throw new TestSuiteException( "Variable name of " + PAR_ELEMENTS + " cannot be empty",
			                              getInterfaceName() + "." + COMMAND );
		}

		return true;
	}

}
