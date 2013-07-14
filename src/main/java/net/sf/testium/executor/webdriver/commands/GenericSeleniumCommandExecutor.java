package net.sf.testium.executor.webdriver.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.configuration.SeleniumConfigurationXmlHandler;
import net.sf.testium.configuration.SeleniumInterfaceConfiguration.SAVE_SOURCE;
import net.sf.testium.executor.general.GenericCommandExecutor;
import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testresult.impl.TestStepCommandResultImpl;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestStepCommand;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;

public abstract class GenericSeleniumCommandExecutor extends GenericCommandExecutor
{
	private SAVE_SOURCE mySavePageSource;
	private SAVE_SOURCE mySaveScreenshot;

	public static final SpecifiedParameter PARSPEC_SAVE_PAGE_SOURCE = new SpecifiedParameter( 
			SeleniumConfigurationXmlHandler.SAVE_PAGESOURCE, String.class, true, true, true, true )
				.setDefaultValue("");
	public static final SpecifiedParameter PARSPEC_SAVE_SCREEN_SHOT = new SpecifiedParameter( 
			SeleniumConfigurationXmlHandler.SAVE_SCREENSHOT, String.class, true, true, true, true )
				.setDefaultValue("");

	/**
	 * @param aVariables
	 * @param parameters
	 * @param result
	 * @throws TestSuiteException
	 */
	abstract protected void doExecute( RunTimeData aVariables,
	                                   ParameterArrayList parameters,
	                                   TestStepCommandResult result) throws Exception;

	/**
	 * @param command
	 * @param parameterSpecs
	 */
	public GenericSeleniumCommandExecutor( String command,
	                               WebInterface aWebInterface,
	                               ArrayList<SpecifiedParameter> parameterSpecs )
	{
		super( command, aWebInterface, parameterSpecs );

		mySavePageSource = aWebInterface.getConfig().getSavePageSource();
		mySaveScreenshot = aWebInterface.getConfig().getSaveScreenShot();
		
		this.addParamSpec( PARSPEC_SAVE_PAGE_SOURCE );
		this.addParamSpec( PARSPEC_SAVE_SCREEN_SHOT );
	}

	protected WebInterface getInterface()
	{
		return (WebInterface) super.getInterface();
	}

	protected WebDriver getDriver() {
		return this.getInterface().getDriver();
	}

	protected WebDriver getDriver( BROWSER_TYPE aBrowserType )
	{
		WebDriver webDriver = this.getInterface().getDriver( aBrowserType );

		return webDriver;
	}

	@Deprecated
	protected WebDriver getDriverAndSetResult( TestStepResult aTestStepResult, BROWSER_TYPE aBrowserType )
	{
		WebDriver webDriver = this.getInterface().getDriver( aBrowserType );

		return webDriver;
	}

	@Override
	public TestStepResult execute( TestStepCommand aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws TestSuiteException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepCommandResult result = new TestStepCommandResultImpl( aStep );

		SAVE_SOURCE savePageSource = obtainSaveSource(mySavePageSource, aVariables, parameters, PARSPEC_SAVE_PAGE_SOURCE);
		SAVE_SOURCE saveScreenShot = obtainSaveSource(mySaveScreenshot, aVariables, parameters, PARSPEC_SAVE_SCREEN_SHOT);

		try	{
			doExecute(aVariables, parameters, result);
			result.setResult( VERDICT.PASSED );

		} catch (UnreachableBrowserException e) {
System.out.println( "Issue #19: UnreachableBrowserException caught when executing command: " + this.getCommand() );
			this.getInterface().destroy(); //????
			failTest(aLogDir, result, e, savePageSource, saveScreenShot);

//		} catch (WebDriverException e) {
//System.out.println( "Issue #19: WebDriverException caught when executing command: " + this.getCommand() );	
//			failTest(aLogDir, result, e);
//			
		} catch (Exception e) {
			failTest(aLogDir, result, e, savePageSource, saveScreenShot);
		}

		if ( savePageSource.equals(SAVE_SOURCE.ALWAYS) ) {
			this.savePageSource( aLogDir, result );
		}

		if ( saveScreenShot.equals(SAVE_SOURCE.ALWAYS) ) {
			this.saveScreenShot( aLogDir, result );
		}

		return result;
	}

	protected void failTest( File aLogDir, TestStepResult aResult, String aMessage,
			SAVE_SOURCE savePageSource, SAVE_SOURCE saveScreenShot )
	{
		aResult.setResult( VERDICT.FAILED );
		if ( ! aMessage.isEmpty() )
		{
			aResult.addComment(aMessage);
		}

		if ( savePageSource.equals(SAVE_SOURCE.ONFAIL)
				|| savePageSource.equals(SAVE_SOURCE.ALWAYS) ) {
			this.savePageSource( aLogDir, aResult );
		}

		if ( saveScreenShot.equals(SAVE_SOURCE.ONFAIL)
				|| saveScreenShot.equals(SAVE_SOURCE.ALWAYS) ) {
			this.saveScreenShot( aLogDir, aResult );
		}
	}

	protected void failTest( File aLogDir, TestStepResult aResult, Exception e,
			SAVE_SOURCE savePageSource, SAVE_SOURCE saveScreenShot )
	{
		failTest( aLogDir, aResult, e.getClass().toString() + ": " + e.getMessage(), savePageSource, saveScreenShot );
	}

	@Deprecated
	protected void failTest( File aLogDir, TestStepResult aResult, String aMessage )
	{
		this.failTest(aLogDir, aResult, aMessage, mySavePageSource, mySaveScreenshot);
	}

	@Deprecated
	protected void failTest( File aLogDir, TestStepResult aResult, Exception e )
	{
		this.failTest(aLogDir, aResult, e, mySavePageSource, mySaveScreenshot);
	}
	
	public boolean savePageSource( File aLogDir, TestStepResult aResult )
	{
		try // We'll try. If it fails we won't fail the test.
		{
			WebDriver driver = this.getDriver();
			if ( driver instanceof RemoteWebDriver ) {
				RemoteWebDriver remDriver = (RemoteWebDriver) driver;
				String pageSource = remDriver.getPageSource();
				
				int i = 0;
				String sourceLogKey = "source";
				File sourceLog = new File( aLogDir, fileNameBase() + sourceLogKey + ".html" );
				while ( sourceLog.exists() && ++i<1000 )
				{
					sourceLog = new File( aLogDir, fileNameBase() + sourceLogKey + "_" + i + ".html" );
				}
			
				FileOutputStream sourceLogOS = new FileOutputStream(sourceLog.getAbsolutePath());
				PrintWriter pw = new PrintWriter(sourceLogOS);
				pw.println(pageSource);
		        pw.flush();

		        aResult.addTestLog(sourceLogKey, sourceLog.getPath());
			}
		}
		catch (Throwable t)
		{
			aResult.addComment( "Source file could not be saved: " + t.getMessage() );
			Trace.print( Trace.SUITE, t );
			return false;
		}
		
		return true;
	}

	public void saveScreenShot(File aLogDir, TestStepResult aResult)
	{
		try // We'll try. If it fails we won't fail the test.
		{
			WebDriver driver = this.getDriver();

			if ( driver != null && driver.getClass().equals(RemoteWebDriver.class) ) {
				driver = new Augmenter().augment(driver);
			}

			if ( driver instanceof RemoteWebDriver )
			{
				TakesScreenshot screenShotDriver = (TakesScreenshot) driver;
				File tmpScreenFile = screenShotDriver.getScreenshotAs(OutputType.FILE);
	
				int i = 0;
				String screenFileKey = "screen";
				File screenFile = new File( aLogDir, fileNameBase() + screenFileKey + ".png" );
				while ( screenFile.exists() && ++i<1000 )
				{
					screenFile = new File( aLogDir, fileNameBase() + screenFileKey + "_" + i + ".png" );
				}
	
				FileUtils.moveFile( tmpScreenFile, screenFile );
		        aResult.addTestLog(screenFileKey, screenFile.getPath());
			}
		} catch ( Throwable t )	{
				aResult.addComment( "Screen file could not be saved: " + t.getMessage() );
				Trace.print( Trace.SUITE, t );
		}
	}

	public void setSavePageSource(SAVE_SOURCE savePageSource) {
		this.mySavePageSource = savePageSource;
	}

	public void setSaveScreenshot(SAVE_SOURCE saveScreenshot) {
		this.mySaveScreenshot = saveScreenshot;
	}

	private String fileNameBase() {
		return this.getCommand() + "_";
	}

	public boolean verifyParameters( ParameterArrayList aParameters)
			   throws TestSuiteException
	{
		Iterator<SpecifiedParameter> paramSpecItr = this.getParametersIterator();
		
		while ( paramSpecItr.hasNext() )
		{
			SpecifiedParameter paramSpec = paramSpecItr.next();
			Parameter par = aParameters.get( paramSpec.getName() );
	
			if ( ! this.verifyParameterExists( par, paramSpec ) )
			{
				continue; // Exception was thrown if it is mandatory
			}

			if ( paramSpec.getType().equals( By.class ) )
			{
				verifyBy(par, paramSpec);
			}
			else
			{
				this.verifyValueOrVariable(par, paramSpec);
			}
		}	
		
		return true;
	}

	protected void verifyBy( Parameter par, SpecifiedParameter paramSpec )
			throws TestSuiteException
	{
		if ( par instanceof ParameterImpl ) {
			ParameterImpl valuePar = (ParameterImpl) par;

			if ( ! By.class.isAssignableFrom( valuePar.getValueType() ) )
			{
				throw new TestSuiteException( "Parameter " + paramSpec.getName() + " must be of type 'id', 'name'," +
				                              " 'linkText', 'partialLinkText', 'tagName', 'xPath'," +
				                              " 'className', or 'cssSelector'",
				                              toString() );
			}
		} // else, we can't verify it.
	}

	/**
	 * @param aVariables
	 * @param parameters
	 * @param aParSpec
	 * 
	 * @return the value as a WebElement (can be null, if optional).
	 * If the element is a SmartWebElement the element is reloaded.
	 * @throws Exception when not an element or when mandatory parameter is not found
	 */
	protected WebElement obtainElement( RunTimeData aVariables,
									  ParameterArrayList parameters,
									  SpecifiedParameter paramSpec ) throws Exception
	{
		WebElement element = (WebElement) super.obtainValue( aVariables, parameters, paramSpec );

		if ( element == null )
		{
			if ( paramSpec.isOptional() )
			{
				return null;
			}
			throw new Exception( "Mandatory element was not found or was not a WebElement: " + paramSpec.getName() );
		}
		// element != null

		return element;
	}

	/**
	 * @param origSource
	 * @param aVariables
	 * @param parameters
	 * @param parSpec
	 * @return
	 */
	private SAVE_SOURCE obtainSaveSource(SAVE_SOURCE origSource, RunTimeData aVariables,
			ParameterArrayList parameters, SpecifiedParameter parSpec) {
		SAVE_SOURCE saveSource = origSource;
		try {
			String savePageSourceString = (String) this.obtainOptionalValue(aVariables, parameters, parSpec);
			if ( ! savePageSourceString.isEmpty() ) {
				saveSource = SAVE_SOURCE.enumOf(savePageSourceString);
			}
		} catch (Exception e1) {
			// We continue with the default setting
		}
		return saveSource;
	}
}
