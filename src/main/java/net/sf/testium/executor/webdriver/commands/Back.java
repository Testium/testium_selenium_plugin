/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 back command
 * 
 * @author Arjan Kranenburg
 *
 */
public class Back extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "back";

    /**
	 * 
	 */
	public Back( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );
	}

	@Override
	protected void doExecute( RunTimeData aVariables,
							  ParameterArrayList parameters,
							  TestStepCommandResult result )
			throws Exception {
		WebDriver webDriver = this.getDriver();

		if ( webDriver instanceof FirefoxDriver ) {
			// Workaround for Selenium issue 3611
			// See also https://code.google.com/p/selenium/issues/detail?id=3611
			ExecuteScript.executeScript(webDriver, "history.go(-1);", new Object[0]);
		} else {
			webDriver.navigate().back();
		}
	}
}
