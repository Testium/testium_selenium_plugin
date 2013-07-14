package net.sf.testium.configuration;
/**
 * 
 */

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import net.sf.testium.configuration.SeleniumInterfaceConfiguration.SAVE_SOURCE;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;

/**
 * @author Arjan Kranenburg
 *
 */
public class SeleniumConfiguration
{
	public static final String PROPERTY_WEBDRIVER_IE_IGNORING_SECURITY_DOMAINS = "webdriver.ie.ignoringsecuritydomains";

	public static enum BROWSER_TYPE
	{
		FIREFOX,
		CHROME,
		HTMLUNIT,
//		IPHONE,
		IE;

		public String toString()
		{
			return super.toString().toLowerCase();
		}

		//overriding valueOf gives a compile error
		public static BROWSER_TYPE enumOf(String aType)
		{
			BROWSER_TYPE value = BROWSER_TYPE.HTMLUNIT;
			if ( aType == null )
			{
				Warning.println( "Browser type is null.\nContinuing with " + value.toString() );
			}
			else if ( aType.isEmpty() )
			{
				Warning.println( "Browser type is empty.\nContinuing with " + value.toString() );
			}
			else
			{
				try
				{
					value = Enum.valueOf( BROWSER_TYPE.class, aType.toUpperCase() );
				}
				catch( IllegalArgumentException iae )
				{
	    			String allTypes = "";
	    			for( BROWSER_TYPE supportedType : BROWSER_TYPE.values() )
	    			{
	    				if (allTypes.isEmpty())
	    				{
	    					allTypes = supportedType.toString();
	    				}
	    				else
	    				{
	    					allTypes += ", " + supportedType;
	    				}
	    			}
					Warning.println( "Browser type " + aType + " is not supported.\nSupported types are " + allTypes + ".\nContinuing with " + value.toString() );
				}
			}

			return value;
		}
	}

	public final static BROWSER_TYPE FIREFOX	= BROWSER_TYPE.FIREFOX;
	public final static BROWSER_TYPE CHROME		= BROWSER_TYPE.CHROME;
	public final static BROWSER_TYPE HTMLUNIT	= BROWSER_TYPE.HTMLUNIT;
//	public final static BROWSER_TYPE IPHONE		= BROWSER_TYPE.IPHONE;
	public final static BROWSER_TYPE IE			= BROWSER_TYPE.IE;

	public final static String BROWSERTYPE		= "browsertype";

	private ArrayList<String> myInterfaceNames;
	private BROWSER_TYPE myBrowser;
	private File mySeleniumLibsDir;
	private URL mySeleniumGridUrl;
	private SAVE_SOURCE mySavePageSource;
	private SAVE_SOURCE mySaveScreenShot;

	/**
	 * @param anInterfaceNames 
	 * @param aBrowser
	 * @param aSeleniumLibsDir
	 * @param aSeleniumGridUrl 
	 */
	public SeleniumConfiguration( 
	                              ArrayList<String> anInterfaceNames,
	                              BROWSER_TYPE aBrowser,
	                              File aSeleniumLibsDir,
	                              URL aSeleniumGridUrl,
	                              SAVE_SOURCE savePageSource,
	                              SAVE_SOURCE saveScreenShot )
	{
	    Trace.println(Trace.CONSTRUCTOR);

	    myInterfaceNames = anInterfaceNames;
	    myBrowser = aBrowser;
	    mySeleniumLibsDir = aSeleniumLibsDir;
		mySeleniumGridUrl = aSeleniumGridUrl;
		mySavePageSource = savePageSource;
		mySaveScreenShot = saveScreenShot;
	}

	/**
	 * @return the myInterfaceNames
	 */
	public ArrayList<String> getInterfaceNames() {
		return myInterfaceNames;
	}

	/**
	 * @return the seleniumLibsDir
	 */
	public File getSeleniumLibsDir()
	{
		return mySeleniumLibsDir;
	}

	/**
	 * @return the Browser type
	 */
	public BROWSER_TYPE getBrowserType()
	{
		return myBrowser;
	}
	
	/**
	 * @return the mySeleniumGridUrl
	 */
	public URL getSeleniumGridUrl() {
		return mySeleniumGridUrl;
	}

	public SAVE_SOURCE getSavePageSource() {
		return this.mySavePageSource;
	}

	public SAVE_SOURCE getSaveScreenShot() {
		return this.mySaveScreenShot;
	}

	/**
	 * In case of Chrome, the chromedriver.exe is found here
	 */
	public static void setChromeDriver( File chromeDriver )
	{
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,chromeDriver.getAbsolutePath());
	}

	/**
	 * In case of InternetExplorer, the ieDriverServer.exe is found here
	 */
	/**
	 * @param ieDriver
	 */
	public static void setIeDriver( File ieDriver )
	{
		System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY,ieDriver.getAbsolutePath());
	}

	/**
	 * In case of InternetExplorer, the security domains can be ignored. 
	 * @see http://code.google.com/p/selenium/wiki/InternetExplorerDriver#Required_Configuration
	 */
	public static void setIeIgnoreSecurityDomains( Boolean aFlag )
	{
		System.setProperty(PROPERTY_WEBDRIVER_IE_IGNORING_SECURITY_DOMAINS,aFlag.toString());
	}
}
