package org.testium.executor.webdriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import org.testium.executor.CustomizableInterface;
import org.testium.executor.TestStepCommandExecutor;
import org.testium.executor.webdriver.commands.*;
import org.testium.systemundertest.SutInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.Trace;

/**
 * Class that represents the Web-based interface of the System Under Test
 * It uses Selenium 2.0 (aka WebDriver) commands to address the interface via a browser.
 *
 * It only opens the browser when needed.
 *  
 * @author Arjan Kranenburg
 *
 */
public class WebInterface implements SutInterface, CustomizableInterface
{
	private WebDriver myDriver;
	private String myDriverName;
	private BROWSER_TYPE myBrowserType;

	private Hashtable<String, TestStepCommandExecutor> myCommandExecutors;

	/**
	 * 
	 */
	public WebInterface( String aName, BROWSER_TYPE aType )
	{
		myDriverName = aName;
		myBrowserType = aType;

		myCommandExecutors = new Hashtable<String, TestStepCommandExecutor>();

		add( new CheckCurrentUrlCommand( this ) );
		add( new CheckTitleCommand( this ) );
		add( new ClearCommand( this ) );
		add( new CloseCommand( this ) );
		add( new FindElementCommand( this ) );
		add( new FindElementsCommand( this ) );
		add( new GetCommand( this ) );
		add( new GetCurrentUrlCommand( this ) );
		add( new GetTitleCommand( this ) );
		add( new BackCommand( this ) );
		add( new ForwardCommand( this ) );
		add( new QuitCommand( this ) );
		add( new SavePageSourceCommand( this ) );
		add( new SendKeysCommand( this ) );
		add( new SubmitCommand( this ) );
	}

	public WebDriver getDriver( )
	{
		if ( myDriver == null )
		{
			createDriver();
		}
		
		return myDriver;
	}

	public void closeWindow( TestStepResult aTestStepResult )
	{
		if ( myDriver == null )
		{
			return; // Nothing to close (getDriver() would have created one first)
		}
		
		this.setTestStepResult(aTestStepResult);
		
		int openWindows = myDriver.getWindowHandles().size();
		myDriver.close();
		if ( openWindows == 1 )
		{
			this.quitDriver( aTestStepResult );
		}

		this.setTestStepResult(null);
	}

	public void quitDriver( TestStepResult aTestStepResult )
	{
		if ( myDriver == null )
		{
			return; // Nothing to quit (getDriver() would have created one first)
		}
		this.setTestStepResult(aTestStepResult);
		
		myDriver.quit();
		this.setTestStepResult(null);

		myDriver = null;
	}

	/**
	 * @param aRemoteWebDriver
	 * @param aTestStepResult
	 */
	public void setTestStepResult( TestStepResult aTestStepResult )
	{
		if ( myDriver == null )
		{
			return;
		}

		if( myDriver.getClass().isAssignableFrom(TestiumLogger.class) )
		{
			TestiumLogger logger = (TestiumLogger) myDriver;
			logger.setTestStepResult(aTestStepResult);
		}
	}

	@Override
	public ArrayList<TestStepCommandExecutor> getCommandExecutors()
	{
		Trace.println( Trace.GETTER );
		Collection<TestStepCommandExecutor> executorCollection = myCommandExecutors.values();
		return new ArrayList<TestStepCommandExecutor>( executorCollection );
	}

	@Override
	public String getInterfaceName()
	{
		return myDriverName;
	}
	

	/**
	 * Methods below is an implementation of SutInterface
	 */

	@Override
	public ArrayList<String> getCommands()
	{
		Trace.println( Trace.GETTER );
		return Collections.list(myCommandExecutors.keys());
	}

	@Override
	public boolean hasCommand(String aCommand)
	{
		Trace.println( Trace.UTIL );
		ArrayList<String> commands = getCommands();
		return commands.contains(aCommand);
	}

	public boolean verifyParameters( String aCommand,
	                                 ParameterArrayList aParameters )
		   throws TestSuiteException
	{
		TestStepCommandExecutor executor = this.getCommandExecutor(aCommand);
		return executor.verifyParameters(aParameters);
	}

	@Override
	public TestStepCommandExecutor getCommandExecutor(String aCommand)
	{
		Trace.println( Trace.GETTER );
		return myCommandExecutors.get(aCommand);
	}

	private void createDriver()
	{
		Trace.println( Trace.UTIL );
		try
		{
			if ( myBrowserType.equals( BROWSER_TYPE.FIREFOX ) )
			{
				myDriver = new TestiumFirefoxDriver();
			}
			else if ( myBrowserType.equals( BROWSER_TYPE.CHROME ) )
			{
				// TODO had a ChromeNotRunningException at the end of the tests
				myDriver = new TestiumChromeDriver();
			}
			else if ( myBrowserType.equals( BROWSER_TYPE.HTMLUNIT ) )
			{
				myDriver = new TestiumHtmlUnitDriver();
			}
	//		else if ( myBrowserType.equals( BROWSER_TYPE.IPHONE ) )
	//		{
	//			try
	//			{
	//				myDriver = new IPhoneDriver();
	//			}
	//			catch (Exception e)
	//			{
	//				// TODO We should end (and error) the test
	//				e.printStackTrace();
	//			}
	//		}
			else if ( myBrowserType.equals( BROWSER_TYPE.IE ) )
			{
				myDriver = new TestiumIEDriver();
			}
		}
		catch ( WebDriverException e )
		{ 
			// TODO How to react?
			throw new Error( "Browser of type " + myBrowserType + " is not found.\n" + e.getMessage() );
		}
	}

	@Override
	public void add( TestStepCommandExecutor aCommandExecutor )
	{
		Trace.println( Trace.UTIL );
		String command = aCommandExecutor.getCommand();
		myCommandExecutors.put(command, aCommandExecutor);
	}

	@Override
	public ParameterImpl createParameter( String aName,
	                                  String aType,
	                                  String aValue )
			throws TestSuiteException
	{
		if ( aValue.isEmpty() )
		{
			throw new TestSuiteException( "Value of " + aName + " cannot be empty for type " + aType,
			                              this.getInterfaceName() );
		}

		if ( aType.equalsIgnoreCase( "id" ) )
		{
			return new ParameterImpl(aName, By.id(aValue) );
		}
		
		if ( aType.equalsIgnoreCase( "name" ) )
		{
			return new ParameterImpl(aName, By.name(aValue) );
		}

		if ( aType.equalsIgnoreCase( "linktext" ) )
		{
			return new ParameterImpl(aName, By.linkText(aValue) );
		}

		if ( aType.equalsIgnoreCase( "partiallinktext" ) )
		{
			return new ParameterImpl(aName, By.partialLinkText(aValue) );
		}

		if ( aType.equalsIgnoreCase( "tagname" ) )
		{
			return new ParameterImpl(aName, By.tagName(aValue) );
		}

		if ( aType.equalsIgnoreCase( "xpath" ) )
		{
			return new ParameterImpl(aName, By.xpath(aValue) );
		}

		if ( aType.equalsIgnoreCase( "classname" ) )
		{
			return new ParameterImpl(aName, By.className(aValue) );
		}

		if ( aType.equalsIgnoreCase( "cssselector" ) )
		{
			return new ParameterImpl(aName, By.cssSelector(aValue) );
		}

		if ( aType.equalsIgnoreCase( "string" ) )
		{
			return new ParameterImpl(aName, (String) aValue);
		}			

		throw new TestSuiteException( "Parameter type " + aType
		                              + " is not supported for interface "
		                              + this.getInterfaceName(), aName );
	}
}
