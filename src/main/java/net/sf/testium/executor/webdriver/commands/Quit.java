/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.configuration.SeleniumInterfaceConfiguration.SAVE_SOURCE;
import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 quit command
 * 
 * @author Arjan Kranenburg
 *
 */
public class Quit extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "quit";

    /**
	 * 
	 */
	public Quit( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );
		
		this.setSavePageSource(SAVE_SOURCE.NEVER);
		this.setSaveScreenshot(SAVE_SOURCE.NEVER);
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		WebInterface iface = this.getInterface();
		iface.quitDriver(result);
	}

}
