/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 getCurrentUrl command and validates the result against a parameter
 * 
 * @author Arjan Kranenburg
 *
 */
public class ExecuteScript extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "executeScript";
	private static final String PAR_SCRIPT = "script";

	public static final SpecifiedParameter PARSPEC_SCRIPT = new SpecifiedParameter( 
			PAR_SCRIPT, String.class, false, true, true, false );


    /**
	 * 
	 */
	public ExecuteScript( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_SCRIPT );
	}

	@Override
	protected void doExecute( RunTimeData aVariables,
							  ParameterArrayList parameters,
							  TestStepResult result )
			throws Exception {
		String script = (String) obtainValue( aVariables, parameters, PARSPEC_SCRIPT );

		executeScript(this.getDriver(), script, new Object[0]);
	}

	/**
	 * @param webDriver
	 * @param script
	 * @throws Exception
	 */
	protected static Object executeScript(WebDriver webDriver, String script, Object[] args)
			throws Exception {
		if ( webDriver instanceof JavascriptExecutor ) {
			return ((JavascriptExecutor) webDriver).executeScript(script, args);
		} else {
			throw new Exception( "The chosen browser-type does not support execution of javaScript" );
		}
	}
}
