/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebDriver;
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
		WebDriver webDriver = this.getDriver();

		String pageSource = webDriver.getPageSource();

		savePageSource(aLogDir, result, pageSource);

		return result;
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		throw new Error( "Method should not have been called" );
	}

	/**
	 * @param aLogDir
	 * @param result
	 * @param pageSource
	 */
	public void savePageSource(File aLogDir, TestStepResult result,
			String pageSource) {
		int i = 0;
		String sourceLogKey = "source";
		File sourceLog = new File( aLogDir, "page_" + sourceLogKey + ".html" );
		while ( sourceLog.exists() && ++i<100 )
		{
			sourceLogKey = "source_" + i;
			sourceLog = new File( aLogDir, "page_" + sourceLogKey + ".html" );
		}
		
		try
		{
			FileOutputStream sourceLogOS = new FileOutputStream(sourceLog.getAbsolutePath());
			PrintWriter pw = new PrintWriter(sourceLogOS);
			pw.println(pageSource);
	        pw.flush();

	        result.addTestLog(sourceLogKey, sourceLog.getPath());
			result.setResult( VERDICT.PASSED );
		}
		catch (FileNotFoundException e)
		{
			result.setComment( "Source file could not be saved: " + e.getMessage() );
			result.setResult( VERDICT.FAILED );
		}
	}
}
