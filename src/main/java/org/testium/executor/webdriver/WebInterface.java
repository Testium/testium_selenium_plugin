package org.testium.executor.webdriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import org.testium.executor.CustomizableInterface;
import org.testium.executor.TestStepCommandExecutor;
import org.testium.executor.webdriver.commands.BackCommand;
import org.testium.executor.webdriver.commands.CheckCurrentUrlCommand;
import org.testium.executor.webdriver.commands.CheckText;
import org.testium.executor.webdriver.commands.CheckTitleCommand;
import org.testium.executor.webdriver.commands.ClearCommand;
import org.testium.executor.webdriver.commands.Click;
import org.testium.executor.webdriver.commands.CloseCommand;
import org.testium.executor.webdriver.commands.FindElementCommand;
import org.testium.executor.webdriver.commands.FindElementsCommand;
import org.testium.executor.webdriver.commands.ForwardCommand;
import org.testium.executor.webdriver.commands.GetCommand;
import org.testium.executor.webdriver.commands.GetCurrentUrlCommand;
import org.testium.executor.webdriver.commands.GetTitleCommand;
import org.testium.executor.webdriver.commands.QuitCommand;
import org.testium.executor.webdriver.commands.SavePageSourceCommand;
import org.testium.executor.webdriver.commands.SendKeysCommand;
import org.testium.executor.webdriver.commands.SubmitCommand;
import org.testium.selenium.FieldPublisher;
import org.testium.systemundertest.SutInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterImpl;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
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
public class WebInterface implements SutInterface, CustomizableInterface, FieldPublisher
{
	private WebDriver myDriver;
	private String myDriverName;
	private final RunTimeData myRtData;
//	private BROWSER_TYPE myBrowserType;

	private Hashtable<String, TestStepCommandExecutor> myCommandExecutors;

	/**
	 * 
	 */
//	public WebInterface( String aName, BROWSER_TYPE aType )
	public WebInterface(String aName, RunTimeData aRtData)
	{
		myDriverName = aName;
//		myBrowserType = aType;
		myRtData = aRtData;

		myCommandExecutors = new Hashtable<String, TestStepCommandExecutor>();

		add( new CheckCurrentUrlCommand( this ) );
		add( new CheckText( this ) );
		add( new CheckTitleCommand( this ) );
		add( new ClearCommand( this ) );
		add( new Click( this ) );
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

	/**
	 * @return the WebDriver. null if not set.
	 */
	public WebDriver getDriver()
	{
		return myDriver;
	}

	public void setDriver( WebDriver aDriver )
	{
		myDriver = aDriver;
	}

	/**
	 * @param aType
	 * @return the WebDriver. It is created if it does not exist.
	 */
	public WebDriver getDriver( BROWSER_TYPE aType )
	{
		if ( myDriver == null )
		{
			createDriver( aType );
		}
		
		return myDriver;
	}

	public void closeWindow( TestStepResult aTestStepResult )
	{
		if ( this.getDriver() == null )
		{
			return; // Nothing to close (getDriver() would have created one first)
		}
		
		this.setTestStepResult(aTestStepResult);
		
		Set<String> windowHandles = this.getDriver().getWindowHandles();
		int openWindows = windowHandles.size();
			this.getDriver().close();
		if ( openWindows == 1 )
		{
			setDriver(null);
		}

		this.setTestStepResult(null);
	}

	public void quitDriver( TestStepResult aTestStepResult )
	{
		if ( this.getDriver() == null )
		{
			return; // Nothing to quit (getDriver() would have created one first)
		}
		this.setTestStepResult(aTestStepResult);

		this.getDriver().quit();
		this.setTestStepResult(null);

		setDriver(null);
	}

	/**
	 * @param aRemoteWebDriver
	 * @param aTestStepResult
	 */
	public void setTestStepResult( TestStepResult aTestStepResult )
	{
		if ( this.getDriver() == null )
		{
			return;
		}

		if( this.getDriver().getClass().isAssignableFrom(TestiumLogger.class) )
		{
			TestiumLogger logger = (TestiumLogger) this.getDriver();
			logger.setTestStepResult(aTestStepResult);
		}
	}

	public ArrayList<TestStepCommandExecutor> getCommandExecutors()
	{
		Trace.println( Trace.GETTER );
		Collection<TestStepCommandExecutor> executorCollection = myCommandExecutors.values();
		return new ArrayList<TestStepCommandExecutor>( executorCollection );
	}

	public String getInterfaceName()
	{
		return myDriverName;
	}
	

	/**
	 * Methods below is an implementation of SutInterface
	 */

	public ArrayList<String> getCommands()
	{
		Trace.println( Trace.GETTER );
		return Collections.list(myCommandExecutors.keys());
	}

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

	public TestStepCommandExecutor getCommandExecutor(String aCommand)
	{
		Trace.println( Trace.GETTER );
		return myCommandExecutors.get(aCommand);
	}

	@SuppressWarnings("deprecation")
	protected void createDriver( BROWSER_TYPE aType )
	{
		Trace.println( Trace.UTIL );
		try
		{
			if ( aType.equals( BROWSER_TYPE.FIREFOX ) )
			{
				setDriver(  new RemoteTestiumDriver( new FirefoxDriver(), this ) );
			}
			else if ( aType.equals( BROWSER_TYPE.CHROME ) )
			{
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();

				// Got rid of the Welcome-page (Getting started with Chrome) by editing:
				// C:\Program Files (x86)\Google\Chrome\Application\master_preferences
				// and setting the show_welcome_page to false
				//
				// Tried that as well with: 
				//				capabilities.setCapability("show-welcome-page", false);
				// and
				//				Hashtable<String, Boolean> prefs = new Hashtable<String, Boolean>();
				//				prefs.put("show-welcome-page", false);
				//				capabilities.setCapability("chrome.prefs", prefs);
				// and
				//				Hashtable<String, Boolean> distribution = new Hashtable<String, Boolean>();
				//				distribution.put("show-welcome-page", false);
				//				Hashtable<String, Object> prefs = new Hashtable<String, Object>();
				//				prefs.put("distribution", distribution);
				//				capabilities.setCapability("chrome.prefs", prefs);
				// and
				//				switches.add( "no-first-run" );
				// But that all failed.
				
				ArrayList<String> switches = new ArrayList<String>();
				switches.add( "disable-translate" );
				capabilities.setCapability("chrome.switches", switches);
				setDriver(  new RemoteTestiumDriver( new ChromeDriver( capabilities ), this ) );
			}
			else if ( aType.equals( BROWSER_TYPE.HTMLUNIT ) )
			{
				setDriver(  new TestiumDriver( new HtmlUnitDriver(), this ) );
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
			else if ( aType.equals( BROWSER_TYPE.IE ) )
			{
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability( InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				setDriver(  new RemoteTestiumDriver( new InternetExplorerDriver( capabilities ), this ) );
			}
		}
		catch ( WebDriverException e )
		{ 
			// TODO How to react?
			throw new Error( "Browser of type " + aType + " is not found.\n" + e.getMessage() );
		}
	}

	public void add( TestStepCommandExecutor aCommandExecutor )
	{
		Trace.println( Trace.UTIL );
		String command = aCommandExecutor.getCommand();
		myCommandExecutors.put(command, aCommandExecutor);
	}

	public ParameterImpl createParameter( String aName,
	                                  String aType,
	                                  String aValue )
			throws TestSuiteException
	{
		if ( aType.equalsIgnoreCase( "string" ) )
		{
			return new ParameterImpl(aName, (String) aValue);
		}			

		if ( aValue.isEmpty() )
		{
			// Strings can be empty, so that's handled before.
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

		throw new TestSuiteException( "Parameter type " + aType
		                              + " is not supported for interface "
		                              + this.getInterfaceName(), aName );
	}

	public void addElement(String varName, WebElement element)
	{
		if( element == null ) {
			return;
		}
		
		RunTimeVariable rtVar = new RunTimeVariable( varName, element );
		myRtData.add( rtVar );
	}

	public WebElement getElement(String varName)
	{
		WebElement element = myRtData.getValueAs( WebElement.class, varName);
		return element;
	}
}
