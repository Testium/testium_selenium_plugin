/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
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
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Executes the Selenium 2.0 findElements command
 *
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
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		BROWSER_TYPE browserType = aVariables.getValueAs(BROWSER_TYPE.class, SeleniumConfiguration.BROWSERTYPE);

		TestStepResult result = new TestStepResult( aStep );
		WebDriver webDriver = this.getDriver(browserType);

		ParameterImpl byPar = (ParameterImpl) parameters.get(PAR_BY);
		By by = byPar.getValueAs(By.class);

		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_ELEMENTS);
		String variableName = variablePar.getVariableName();

		List<WebElement> elements = webDriver.findElements(by);

		RunTimeVariable rtVariable = new RunTimeVariable( variableName, elements );
		aVariables.add(rtVariable);

		result.setResult( VERDICT.PASSED );

		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		// Check the Value Parameter
		Parameter valuePar_tmp = aParameters.get(PAR_BY);
		if ( valuePar_tmp == null )
		{
			throw new TestSuiteException( "Parameter " + PAR_BY + " is not set",
			                              getInterfaceName() + "." + COMMAND );
		}

		if ( ! ParameterImpl.class.isInstance( valuePar_tmp ) )
		{
			throw new TestSuiteException( "Parameter " + PAR_BY + " is not a value",
			                              getInterfaceName() + "." + COMMAND );
		}

		ParameterImpl valuePar = (ParameterImpl) valuePar_tmp;
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

		verifyParameterVariable(elementsPar);

		return true;
	}

}
