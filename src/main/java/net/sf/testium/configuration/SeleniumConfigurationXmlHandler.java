package net.sf.testium.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import net.sf.testium.Testium;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.configuration.SeleniumInterfaceConfiguration.SAVE_SOURCE;

import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author Arjan Kranenburg 
 * 
 *  <SeleniumConfiguration>
 *    <DefaultBrowser>...</DefaultBrowser>
 *    <ChromeDriver>...</ChromeDriver>
 *    <IeIntroduceFlakinessByIgnoringSecurityDomains>...</IeIntroduceFlakinessByIgnoringSecurityDomains>
 *    <IePathToDriverServer>...</IePathToDriverServer>
 *    <SavePageSource>...</SavePageSource>
 *    <SaveScreenshot>...</SaveScreenshot>
 *    <SeleniumLibsDir>...</SeleniumLibsDir>
 *    <SeleniumInterfaces>...</SeleniumInterfaces>
 *    <SeleniumGridUrl>...</SeleniumGridUrl>
 *  ...
 *  </SeleniumConfiguration>
 * 
 */

public class SeleniumConfigurationXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "SeleniumConfiguration";

	private static final String DEF_BROWSER_ELEMENT = "DefaultBrowser";
	private static final String IE_IGNORING_SECURITY_DOMAINS_ELEMENT = "IeIntroduceFlakinessByIgnoringSecurityDomains";
	private static final String IE_PATH_TO_DRIVER_SERVER_ELEMENT = "IePathToDriverServer";
	public static final String SAVE_PAGESOURCE = "SavePageSource"; // NEVER, ONFAIL, ALWAYS
	public static final String SAVE_SCREENSHOT = "SaveScreenshot"; // NEVER, ONFAIL, ALWAYS
	private static final String SELENIUM_LIBS_DIR_ELEMENT = "SeleniumLibsDir";
	private static final String SELENIUM_GRID_URL_ELEMENT = "SeleniumGridUrl";
	private static final String CHROME_DRIVER_ELEMENT = "ChromeDriver";

	private GenericTagAndStringXmlHandler myDefaultBrowserXmlHandler;
	private GenericTagAndStringXmlHandler mySeleniumLibsDirXmlHandler;
	private GenericTagAndStringXmlHandler myChromeDriverXmlHandler;
	private GenericTagAndStringXmlHandler mySavePageSourceXmlHandler;
	private GenericTagAndStringXmlHandler mySaveScreenShotXmlHandler;
	private GenericTagAndStringXmlHandler myIeIgnoreSecurityDomainsXmlHandler;
	private GenericTagAndStringXmlHandler myIePathToDriverServerXmlHandler;
	private GenericTagAndStringXmlHandler mySeleniumGridUrlXmlHandler;
	private SeleniumInterfacesXmlHandler myInterfacesXmlHandler;
	
	private BROWSER_TYPE myDefaultBrowser = BROWSER_TYPE.HTMLUNIT;
	private File mySeleniumLibsDir;
	private URL mySeleniumGridUrl;
	private ArrayList<String> myInterfaceNames;
	private SAVE_SOURCE mySavePageSource;
	private SAVE_SOURCE mySaveScreenShot;

	private RunTimeData myRtData;

	public SeleniumConfigurationXmlHandler( XMLReader anXmlReader, RunTimeData anRtData ) {
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		File pluginsDir = anRtData.getValueAsFile(Testium.PLUGINSDIR);
		mySeleniumLibsDir = new File( pluginsDir, "SeleniumLibs" );
		
		mySeleniumGridUrl = null;

		File baseDir = anRtData.getValueAsFile(Testium.BASEDIR);
		File dataDir = new File( baseDir, "../../src/data" );
		SeleniumConfiguration.setChromeDriver( new File( dataDir, "chromedriver.exe" ) );
		SeleniumConfiguration.setIeDriver( new File( dataDir, "IEDriverServer.exe" ) );

		SeleniumConfiguration.setIeIgnoreSecurityDomains(false);

		myRtData = anRtData;
		
	    myDefaultBrowserXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, DEF_BROWSER_ELEMENT);
		this.addElementHandler(myDefaultBrowserXmlHandler);

		mySeleniumLibsDirXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SELENIUM_LIBS_DIR_ELEMENT);
		this.addElementHandler(mySeleniumLibsDirXmlHandler);

		mySeleniumGridUrlXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SELENIUM_GRID_URL_ELEMENT);
		this.addElementHandler(mySeleniumGridUrlXmlHandler);

		myChromeDriverXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, CHROME_DRIVER_ELEMENT);
		this.addElementHandler(myChromeDriverXmlHandler);

		myIeIgnoreSecurityDomainsXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, IE_IGNORING_SECURITY_DOMAINS_ELEMENT);
		this.addElementHandler(myIeIgnoreSecurityDomainsXmlHandler);

		myIePathToDriverServerXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, IE_PATH_TO_DRIVER_SERVER_ELEMENT);
		this.addElementHandler(myIePathToDriverServerXmlHandler);

		mySavePageSourceXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SAVE_PAGESOURCE);
		this.addElementHandler(mySavePageSourceXmlHandler);

		mySaveScreenShotXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SAVE_SCREENSHOT);
		this.addElementHandler(mySaveScreenShotXmlHandler);

		myInterfacesXmlHandler = new SeleniumInterfacesXmlHandler( anXmlReader );
		this.addElementHandler(myInterfacesXmlHandler);
	}

	@Override
	public void handleStartElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleCharacters(String aValue)
	{
		// nop
	}

	@Override
	public void handleEndElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void processElementAttributes(String aQualifiedName, Attributes att)
	{
		// nop
	}

	@Override
	public void handleGoToChildElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler)
			throws TestSuiteException
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
		    	      aQualifiedName + " )", true);
		    
		if (aQualifiedName.equalsIgnoreCase( DEF_BROWSER_ELEMENT ))
    	{
			BROWSER_TYPE browserType = BROWSER_TYPE.enumOf( myDefaultBrowserXmlHandler.getValue() );
			myDefaultBrowser = browserType;
			myDefaultBrowserXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( SELENIUM_LIBS_DIR_ELEMENT ))
    	{
			String seleniumLibsDirName = mySeleniumLibsDirXmlHandler.getValue();
			seleniumLibsDirName = myRtData.substituteVars(seleniumLibsDirName);
			mySeleniumLibsDir = new File( seleniumLibsDirName );

			mySeleniumLibsDirXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( SELENIUM_GRID_URL_ELEMENT ))
    	{
			String seleniumGridUrl = mySeleniumGridUrlXmlHandler.getValue();
			seleniumGridUrl = myRtData.substituteVars(seleniumGridUrl);
			try {
				mySeleniumGridUrl = new URL( seleniumGridUrl );
			} catch (MalformedURLException e) {
				throw new TestSuiteException( "\"" + SELENIUM_GRID_URL_ELEMENT + "\" is malformed: " + e.getMessage(), e );
			}

			mySeleniumGridUrlXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( CHROME_DRIVER_ELEMENT ))
    	{
			String chromeDriverName = myChromeDriverXmlHandler.getValue();
			chromeDriverName = myRtData.substituteVars(chromeDriverName);
			SeleniumConfiguration.setChromeDriver( new File( chromeDriverName ) );

			myChromeDriverXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( IE_IGNORING_SECURITY_DOMAINS_ELEMENT ))
    	{
			String ignoreSecurityDomainsFlag = myIeIgnoreSecurityDomainsXmlHandler.getValue();
			SeleniumConfiguration.setIeIgnoreSecurityDomains( new Boolean(ignoreSecurityDomainsFlag) );

			myIeIgnoreSecurityDomainsXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase( IE_PATH_TO_DRIVER_SERVER_ELEMENT ))
    	{
			String ieDriverName = myIePathToDriverServerXmlHandler.getValue();
			ieDriverName = myRtData.substituteVars(ieDriverName);
			SeleniumConfiguration.setIeDriver( new File( ieDriverName ) );

			myIePathToDriverServerXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( SAVE_PAGESOURCE ))
    	{
			String savePageSource = mySavePageSourceXmlHandler.getValue();
			mySavePageSource = SAVE_SOURCE.enumOf(savePageSource);
			mySavePageSourceXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( SAVE_SCREENSHOT ))
    	{
			String saveScreenshots = mySaveScreenShotXmlHandler.getValue();
			mySaveScreenShot = SAVE_SOURCE.enumOf(saveScreenshots);
			mySaveScreenShotXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( myInterfacesXmlHandler.getStartElement() ))
    	{
			myInterfaceNames = myInterfacesXmlHandler.getInterfaceNames();
			myInterfacesXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}
	
	public SeleniumConfiguration getConfiguration()
	{
		return new SeleniumConfiguration( myInterfaceNames, myDefaultBrowser, mySeleniumLibsDir, mySeleniumGridUrl, mySavePageSource, mySaveScreenShot );
	}

}
