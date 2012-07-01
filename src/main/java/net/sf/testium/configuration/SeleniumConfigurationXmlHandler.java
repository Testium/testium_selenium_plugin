package net.sf.testium.configuration;

import java.io.File;

import net.sf.testium.Testium;
import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.executor.SupportedInterfaceList;
import net.sf.testium.executor.TestStepMetaExecutor;

import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;
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
 *    <IeIgnoreProtectedModeSettings>...</IeIgnoreProtectedModeSettings>
 *    <SavePageSource>...</SavePageSource>
 *    <SaveScreenshot>...</SaveScreenshot>
 *    <SeleniumLibsDir>...</SeleniumLibsDir>
 *    <SeleniumInterfaces>...</SeleniumInterfaces>
 *  ...
 *  </SeleniumConfiguration>
 * 
 */

public class SeleniumConfigurationXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "SeleniumConfiguration";

	private static final String DEF_BROWSER_ELEMENT = "DefaultBrowser";
	private static final String IE_IGNORE_PROTECTED_MODE_SETTINGS_ELEMENT = "IeIgnoreProtectedModeSettings";
	private static final String SAVE_PAGESOURCE = "SavePageSource"; // NEVER, ONFAIL, ALWAYS
	private static final String SAVE_SCREENSHOT = "SaveScreenshot"; // NEVER, ONFAIL, ALWAYS
	private static final String SELENIUM_LIBS_DIR_ELEMENT = "SeleniumLibsDir";
	private static final String CHROME_DRIVER_ELEMENT = "ChromeDriver";

	private GenericTagAndStringXmlHandler myDefaultBrowserXmlHandler;
	private GenericTagAndStringXmlHandler mySeleniumLibsDirXmlHandler;
	private GenericTagAndStringXmlHandler myChromeDriverXmlHandler;
	private GenericTagAndStringXmlHandler mySavePageSourceXmlHandler;
	private GenericTagAndStringXmlHandler mySaveScreenShotXmlHandler;
	private SeleniumInterfacesXmlHandler myInterfacesXmlHandler;
	
	private BROWSER_TYPE myDefaultBrowser = BROWSER_TYPE.HTMLUNIT;
	private File mySeleniumLibsDir;
	private File myChromeDriver;

	private RunTimeData myRtData;
	
	public SeleniumConfigurationXmlHandler( XMLReader anXmlReader, 
	                                        SupportedInterfaceList anInterfaceList,
	                                        TestStepMetaExecutor aTestStepMetaExecutor,
	                                        RunTimeData anRtData )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		File pluginsDir = anRtData.getValueAsFile(Testium.PLUGINSDIR);
		mySeleniumLibsDir = new File( pluginsDir, "SeleniumLibs" );
		myChromeDriver = new File( mySeleniumLibsDir, "chromedriver.exe" );

		myRtData = anRtData;
		
	    myDefaultBrowserXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, DEF_BROWSER_ELEMENT);
		this.addElementHandler(myDefaultBrowserXmlHandler);

		mySeleniumLibsDirXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SELENIUM_LIBS_DIR_ELEMENT);
		this.addElementHandler(mySeleniumLibsDirXmlHandler);

		myChromeDriverXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, CHROME_DRIVER_ELEMENT);
		this.addElementHandler(myChromeDriverXmlHandler);

		mySavePageSourceXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SAVE_PAGESOURCE);
		this.addElementHandler(mySavePageSourceXmlHandler);
		// Default value
		RunTimeVariable savePagesourceVar = new RunTimeVariable(SeleniumConfiguration.VARNAME_SAVEPAGESOURCE, "NEVER");
		this.myRtData.add(savePagesourceVar);

		mySaveScreenShotXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SAVE_SCREENSHOT);
		this.addElementHandler(mySaveScreenShotXmlHandler);
		// Default value
		RunTimeVariable saveScreenshotsVar = new RunTimeVariable(SeleniumConfiguration.VARNAME_SAVESCREENSHOT, "NEVER");
		this.myRtData.add(saveScreenshotsVar);

		myInterfacesXmlHandler = new SeleniumInterfacesXmlHandler( anXmlReader,
		                                                           anInterfaceList,
		                                                           aTestStepMetaExecutor,
		                                                           anRtData);
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
//			myDefaultBrowser = BROWSER_TYPE.valueOf( BROWSER_TYPE.class, myDefaultBrowserXmlHandler.getValue() );
//			myInterfacesXmlHandler.setDefaultBrowser( myDefaultBrowser );
			BROWSER_TYPE browserType = BROWSER_TYPE.enumOf( BROWSER_TYPE.class, myDefaultBrowserXmlHandler.getValue() );
			RunTimeVariable browserTypeVar = new RunTimeVariable(SeleniumConfiguration.BROWSERTYPE, browserType);
			myRtData.add(browserTypeVar);

			myDefaultBrowserXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( SELENIUM_LIBS_DIR_ELEMENT ))
    	{
			String SeleniumLibsDirName = mySeleniumLibsDirXmlHandler.getValue();
			SeleniumLibsDirName = myRtData.substituteVars(SeleniumLibsDirName);
			mySeleniumLibsDir = new File( SeleniumLibsDirName );

			mySeleniumLibsDirXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( CHROME_DRIVER_ELEMENT ))
    	{
			String ChromeDriverName = myChromeDriverXmlHandler.getValue();
			ChromeDriverName = myRtData.substituteVars(ChromeDriverName);
			myChromeDriver = new File( ChromeDriverName );

			myChromeDriverXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( SAVE_PAGESOURCE ))
    	{
			String savePageSource = mySavePageSourceXmlHandler.getValue();
			if ( !savePageSource.equalsIgnoreCase("NEVER")
					&& !savePageSource.equalsIgnoreCase("ONFAIL")
					&& !savePageSource.equalsIgnoreCase("ALWAYS") ) {
				throw new TestSuiteException( "\"" + savePageSource + "\" is not allowed for " + SAVE_PAGESOURCE + ". Only NEVER, ONFAIL, or ALWAYS" );
			}
			
			RunTimeVariable savePageSourceVar = new RunTimeVariable(SeleniumConfiguration.VARNAME_SAVEPAGESOURCE,
					savePageSource.toUpperCase());
			this.myRtData.add(savePageSourceVar);

			mySavePageSourceXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( SAVE_SCREENSHOT ))
    	{
			String saveScreenshots = mySaveScreenShotXmlHandler.getValue();
			if ( !saveScreenshots.equalsIgnoreCase("NEVER")
					&& !saveScreenshots.equalsIgnoreCase("ONFAIL")
					&& !saveScreenshots.equalsIgnoreCase("ALWAYS") ) {
				throw new TestSuiteException( "\"" + saveScreenshots + "\" is not allowed for " + SAVE_SCREENSHOT + ". Only NEVER, ONFAIL, or ALWAYS" );
			}
			
			RunTimeVariable saveScreenshotsVar = new RunTimeVariable(SeleniumConfiguration.VARNAME_SAVESCREENSHOT,
						saveScreenshots.toUpperCase());
			this.myRtData.add(saveScreenshotsVar);

			mySaveScreenShotXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( myInterfacesXmlHandler.getStartElement() ))
    	{
			// The interfaceList is already updated
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
		// In case of Chrome, the chromedriver.exe is found here
		System.setProperty("webdriver.chrome.driver",myChromeDriver.getPath());

		return new SeleniumConfiguration( myDefaultBrowser, mySeleniumLibsDir );
	}
}
