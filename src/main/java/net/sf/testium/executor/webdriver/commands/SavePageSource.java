/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;
import java.util.ArrayList;

import net.sf.testium.configuration.SeleniumInterfaceConfiguration.SAVE_SOURCE;
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

	public static final SpecifiedParameter PARSPEC_FILE_NAME = new SpecifiedParameter( 
			"filename", String.class, "Name of the file to use when saving the page-source", true, true, true, false )
				.setDefaultValue("");

    /**
	 * 
	 */
	public SavePageSource( WebInterface aWebInterface )	{
		super( COMMAND, "Saves the page source to a specified filename", aWebInterface, new ArrayList<SpecifiedParameter>() );
		
		this.getParameterSpecs().remove( GenericSeleniumCommandExecutor.PARSPEC_SAVE_PAGE_SOURCE );
		this.setSaveScreenshot(SAVE_SOURCE.NEVER);

		this.addParamSpec( PARSPEC_FILE_NAME );
	}

	@Override
	public TestStepResult execute( TestStepCommand aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepCommandResultImpl( aStep );
		String fileName = "";
		try {
			fileName = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_FILE_NAME);
		} catch (Exception e) {
			result.setResult( VERDICT.ERROR );
			result.addComment( e.getMessage() );
			// We'll try to continue as well. Verdict will remain ERROR & default filename is used.
		}

		if ( this.savePageSource(aLogDir, fileName, result) ) {
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
