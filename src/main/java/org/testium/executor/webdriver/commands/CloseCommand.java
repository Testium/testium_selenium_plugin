/**
 * 
 */
package org.testium.executor.webdriver.commands;

import java.io.File;

import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 close command
 * 
 * @author Arjan Kranenburg
 *
 */
public class CloseCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "close";

    /**
	 * 
	 */
	public CloseCommand( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface);
	}

	@Override
	public TestStepResult execute( TestStep aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );

		WebInterface iface = this.getInterface();
		iface.closeWindow(result);
		
		result.setResult( VERDICT.PASSED );
		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		return true;
	}

}
