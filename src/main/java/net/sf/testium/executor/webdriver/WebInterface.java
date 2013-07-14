package net.sf.testium.executor.webdriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.configuration.SeleniumInterfaceConfiguration;
import net.sf.testium.executor.CustomInterface;
import net.sf.testium.executor.webdriver.commands.*;
import net.sf.testium.selenium.FieldPublisher;
import net.sf.testium.selenium.WebDriverInterface;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testtoolinterfaces.testresult.TestStepResult;
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
public class WebInterface extends CustomInterface implements FieldPublisher, WebDriverInterface
{
	private WebDriver myDriver;
	private final RunTimeData myRtData;
	private SeleniumInterfaceConfiguration config;

	/**
	 * 
	 */
	public WebInterface(String aName, RunTimeData aRtData, SeleniumInterfaceConfiguration ifConfig)
	{
		super( aName );
		myRtData = aRtData;

		this.config = ifConfig;

		add( new Back( this ) );
		add( new CentralizeItem( this ) );
		add( new CheckAttribute( this ) );
		add( new CheckCurrentUrl( this ) );
		add( new CheckEditable( this ) );
		add( new CheckSelected( this ) );
		add( new CheckText( this ) );
		add( new CheckTitle( this ) );
		add( new Clear( this ) );
		add( new Click( this ) );
		add( new Close( this ) );
		add( new CtrlClick( this ) );
		add( new DefineElement( this ) );
		add( new DefineElementList( this ) );
		add( new ExecuteScript( this ) );
		add( new FindElement( this ) );
		add( new FindElements( this ) );
		add( new Forward( this ) );
		add( new Get( this ) );
		add( new GetAttribute( this ) );
		add( new GetCurrentUrl( this ) );
		add( new GetText( this ) );
		add( new GetTitle( this ) );
		add( new LoadElementDefinitions( this ) );
		add( new Quit( this ) );
		add( new Refresh( this ) );
		add( new SavePageSource( this ) );
		add( new SelectValue( this ) );
		add( new SendKeys( this ) );
		add( new SetByVariable( this ) );
		add( new Submit( this ) );
		add( new SwitchTo( this ) );
		add( new WaitFor( this ) );
		add( new WaitForPresent( this ) );
		add( new WaitForVisible( this ) );
		add( new WaitForUrl( this ) );
	}

	public SeleniumInterfaceConfiguration getConfig() {
		return config;
	}

	public String getBaseUrl()
	{
		return this.config.getBaseUrl();
	}

	/**
	 * @param aType
	 * @return the WebDriver of the specified type. It is created if it does not exist.
	 */
	public WebDriver getDriver( BROWSER_TYPE aType )
	{
		if ( myDriver == null )
		{
			createDriver( aType );
		}

		return myDriver;
	}

	/**
	 * @return the WebDriver, null if it is not set.
	 */
	public WebDriver getDriver()
	{
		return myDriver;
	}

	protected void setDriver( WebDriver aDriver )
	{
		myDriver = aDriver;
	}

	public void closeWindow( TestStepResult aTestStepResult )
	{
		if ( this.myDriver == null )
		{
			return; // Nothing to close (getDriver() would have created one first)
		}
		
		this.setTestStepResult(aTestStepResult);
		
		try {
			Set<String> windowHandles = this.myDriver.getWindowHandles();
			int openWindows = windowHandles.size();
			this.myDriver.close();
			if ( openWindows == 1 )
			{
				setDriver(null);
			}
		} catch ( UnreachableBrowserException ignored ) {
System.out.println( "Issue #19: UnreachableBrowserException caught when quiting the browser." );
			setDriver(null);
		} catch ( WebDriverException ignored ) {
System.out.println( "Issue #19: WebDriverException caught when quiting the browser." );
			setDriver(null);
		}

		this.setTestStepResult(null);
	}
	
	public void quitDriver( TestStepResult aTestStepResult )
	{
		if ( this.myDriver == null )
		{
			return; // Nothing to quit (getDriver() would have created one first)
		}
		this.setTestStepResult(aTestStepResult);

		try {
			this.myDriver.quit();
		} catch ( UnreachableBrowserException ignored ) {
System.out.println( "Issue #19: UnreachableBrowserException caught when quiting the browser." );
			// Below we'll set the driver to null
		} catch ( WebDriverException ignored ) {
System.out.println( "Issue #19: WebDriverException caught when quiting the browser." );
			// Below we'll set the driver to null
		}
		this.setTestStepResult(null);

		setDriver(null);
	}

	/**
	 * @param aRemoteWebDriver
	 * @param aTestStepResult
	 */
	public void setTestStepResult( TestStepResult aTestStepResult )
	{
		if ( this.myDriver == null )
		{
			return;
		}
	}

	protected void createDriver( BROWSER_TYPE aType ) {
		Trace.println( Trace.UTIL );

		try {
			if ( config.getSeleniumGridUrl() == null ) {
				createLocalDriver( aType );
			} else {
				createRemoteDriver( aType );
			}
		} catch ( WebDriverException e ) { 
System.out.println("WebDriverException caught: " + e.getLocalizedMessage());
			// TODO How to react?
			throw new Error( "Browser of type " + aType + " is not found.\n" + e.getMessage() );
		} catch ( Exception ex ) {
			System.out.println("Exception caught: " + ex.getLocalizedMessage());
		}
	}

	private void createRemoteDriver(BROWSER_TYPE aType) {
		DesiredCapabilities capability = DesiredCapabilities.firefox(); // default
		capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		
		if ( aType.equals( BROWSER_TYPE.CHROME ) )
		{
			capability = DesiredCapabilities.chrome();
			capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);

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
			capability.setCapability("chrome.switches", switches);
		}
		else if ( aType.equals( BROWSER_TYPE.HTMLUNIT ) )
		{
			capability = DesiredCapabilities.htmlUnit();
		}
		else if ( aType.equals( BROWSER_TYPE.IE ) )
		{
			capability = DesiredCapabilities.internetExplorer();
			capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);

			String ignoreSecurityDomains = System.getProperty(SeleniumConfiguration.PROPERTY_WEBDRIVER_IE_IGNORING_SECURITY_DOMAINS);
			capability.setCapability( InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, new Boolean(ignoreSecurityDomains) );
		}

		WebDriver driver = new RemoteWebDriver(config.getSeleniumGridUrl(), capability);
		setDriver(driver);
	}

	private void createLocalDriver(BROWSER_TYPE aType) {
		WebDriver driver = new FirefoxDriver(); // default

		if ( aType.equals( BROWSER_TYPE.CHROME ) )
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
			driver = new ChromeDriver( capabilities );
		}
		else if ( aType.equals( BROWSER_TYPE.HTMLUNIT ) )
		{
			driver = new HtmlUnitDriver();
		}
//		else if ( myBrowserType.equals( BROWSER_TYPE.IPHONE ) )
//		{
//			driver = new IPhoneDriver();
//		}
		else if ( aType.equals( BROWSER_TYPE.IE ) )
		{
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();

			String ignoreSecurityDomains = System.getProperty(SeleniumConfiguration.PROPERTY_WEBDRIVER_IE_IGNORING_SECURITY_DOMAINS);
			capabilities.setCapability( InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, new Boolean(ignoreSecurityDomains) );

			driver = new InternetExplorerDriver( capabilities );
		}

		setDriver(driver);
	}

	public ParameterImpl createParameter( String aName,
	                                  String aType,
	                                  String aValue )
			throws TestSuiteException
	{
		try
		{
			return super.createParameter(aName, aType, aValue);
		}
		catch ( TestSuiteException ignored )
		{
			// continue below
		}

		By by = WebInterface.getBy(aType, aValue);
		if ( by != null ) {
			return new ParameterImpl(aName, by );
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

	public void addElement(String varName, List<WebElement> elements) {
		if( elements == null ) {
			return;
		}
		
		RunTimeVariable rtVar = new RunTimeVariable( varName, elements );
		myRtData.add( rtVar );
	}

	public WebElement getElement(String varName)
	{
		WebElement element = myRtData.getValueAs( WebElement.class, varName);
		return element;
	}

	@Override
	public String toString()
	{
		return this.getInterfaceName();
	}

	public void destroy()
	{
		if ( this.myDriver == null )
		{
			return; // Nothing to destroy (getDriver() would have created one first)
		}
		
		this.myDriver.quit();
		setDriver(null);
	}
	
	public static By getBy( String aType, String aValue )
	{
//		String value = myRtData.substituteVars(aValue);
		if ( aType.equalsIgnoreCase( "id" ) )
		{
			return By.id(aValue);
		}
		
		if ( aType.equalsIgnoreCase( "name" ) )
		{
			return By.name(aValue);
		}

		if ( aType.equalsIgnoreCase( "linktext" ) )
		{
			return By.linkText(aValue);
		}

		if ( aType.equalsIgnoreCase( "partiallinktext" ) )
		{
			return By.partialLinkText(aValue);
		}

		if ( aType.equalsIgnoreCase( "tagname" ) )
		{
			return By.tagName(aValue);
		}

		if ( aType.equalsIgnoreCase( "xpath" ) )
		{
			return By.xpath(aValue);
		}

		if ( aType.equalsIgnoreCase( "classname" ) )
		{
			return By.className(aValue);
		}

		if ( aType.equalsIgnoreCase( "cssselector" ) )
		{
			return By.cssSelector(aValue);
		}
		
		//else
		return null;
	}
}
