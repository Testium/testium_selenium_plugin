/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.openqa.selenium.WebDriver;
import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Executes the Selenium 2.0 savePageSource command
 * 
 * @author Arjan Kranenburg
 *
 */
public class SavePageSourceCommand extends WebDriverCommandExecutor
{
	private static final String COMMAND = "savePageSource";

    /**
	 * 
	 */
	public SavePageSourceCommand( WebInterface aWebInterface )
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
		WebDriver webDriver = this.getDriverAndSetResult(result, browserType);

		String pageSource = webDriver.getPageSource();
		setTestStepResult( null, browserType );

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

		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
	{
		return true;
	}

}
