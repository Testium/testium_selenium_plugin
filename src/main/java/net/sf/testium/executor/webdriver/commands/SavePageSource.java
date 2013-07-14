/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;
import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.impl.TestStepCommandResultImpl;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 savePageSource command
 * 
 * @author Arjan Kranenburg
 *
 */
public class SavePageSource extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "savePageSource";

    /**
	 * 
	 */
	public SavePageSource( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );
	}

	@Override
	public TestStepResult execute( TestStepCommand aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepCommandResultImpl( aStep );
		if ( this.savePageSource(aLogDir, result) ) {
			result.setResult( VERDICT.PASSED );			
		} else {
			result.setResult( VERDICT.FAILED );
		}

		return result;
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		throw new Error( "Method should not have been called" );
	}
}
