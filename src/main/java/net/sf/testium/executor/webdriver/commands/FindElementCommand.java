/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

import net.sf.testium.selenium.WebDriverInterface;

import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;
import net.sf.testium.selenium.SimplePageElement;
import net.sf.testium.selenium.SmartWebElement;

/**
 * Executes the Selenium 2 findElement command
 * 
 * @author Arjan Kranenburg
 *
 */
public class FindElementCommand extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "findElement";

	public static final SpecifiedParameter PARSPEC_BY = new SpecifiedParameter( 
			"by", By.class, false, true, true, false );

	public static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			"element", String.class, false, true, false, false );

	public static final SpecifiedParameter PARSPEC_BASEELEMENT = new SpecifiedParameter( 
			"baseElement", WebElement.class, true, false, true, false );


    /**
	 * 
	 */
	public FindElementCommand( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_BY );
		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_BASEELEMENT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception
	{

		BROWSER_TYPE browserType = aVariables.getValueAs(BROWSER_TYPE.class, SeleniumConfiguration.BROWSERTYPE);
		WebDriver driver = this.getDriver(browserType);

		By by = (By) obtainValue(aVariables, parameters, PARSPEC_BY);
		String elementName = (String) obtainValue(aVariables, parameters, PARSPEC_ELEMENT);

		try
		{
			WebElement element;
			WebElement baseElement = this.obtainElement(aVariables, parameters, PARSPEC_BASEELEMENT);
			if ( baseElement != null )
			{
				element = baseElement.findElement(by);
			}
			else
			{
				element = driver.findElement(by);
			}
			SmartWebElement smartElement = new SimplePageElement( by, (WebDriverInterface) this.getInterface(), element );

			RunTimeVariable rtVariable = new RunTimeVariable( elementName, smartElement );
			aVariables.add(rtVariable);
		}
		catch ( NoSuchElementException nsee )
		{
			throw new TestSuiteException( "Element could not be found",
										  result.getCommand(),
										  result.getSequenceNr(),
										  nsee );
		}
	}
}
