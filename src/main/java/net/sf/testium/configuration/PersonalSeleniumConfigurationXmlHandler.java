package net.sf.testium.configuration;

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
 *    <SavePageSource>...</SavePageSource>
 *    <SaveScreenshot>...</SaveScreenshot>
 *    <SeleniumInterfaces>...</SeleniumInterfaces>
 *  ...
 *  </SeleniumConfiguration>
 * 
 */

public class PersonalSeleniumConfigurationXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "SeleniumConfiguration";

	private static final String DEF_BROWSER_ELEMENT = "DefaultBrowser";
	private static final String SAVE_PAGESOURCE = "SavePageSource"; // NEVER, ONFAIL, ALWAYS
	private static final String SAVE_SCREENSHOT = "SaveScreenshot"; // NEVER, ONFAIL, ALWAYS

	private GenericTagAndStringXmlHandler myDefaultBrowserXmlHandler;
	private GenericTagAndStringXmlHandler mySavePageSourceXmlHandler;
	private GenericTagAndStringXmlHandler mySaveScreenShotXmlHandler;
	private SeleniumInterfacesXmlHandler myInterfacesXmlHandler;
	
	private BROWSER_TYPE myDefaultBrowser = BROWSER_TYPE.HTMLUNIT;

	private RunTimeData myRtData;
	
	public PersonalSeleniumConfigurationXmlHandler( XMLReader anXmlReader, 
	                                        SupportedInterfaceList anInterfaceList,
	                                        TestStepMetaExecutor aTestStepMetaExecutor,
	                                        RunTimeData anRtData )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		myRtData = anRtData;
		
	    myDefaultBrowserXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, DEF_BROWSER_ELEMENT);
		this.addElementHandler(myDefaultBrowserXmlHandler);

		mySavePageSourceXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SAVE_PAGESOURCE);
		this.addElementHandler(mySavePageSourceXmlHandler);

		mySaveScreenShotXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SAVE_SCREENSHOT);
		this.addElementHandler(mySaveScreenShotXmlHandler);

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
			BROWSER_TYPE browserType = BROWSER_TYPE.enumOf( myDefaultBrowserXmlHandler.getValue() );
			RunTimeVariable browserTypeVar = new RunTimeVariable(SeleniumConfiguration.BROWSERTYPE, browserType);
			myRtData.add(browserTypeVar);

			myDefaultBrowserXmlHandler.reset();	
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
		return new SeleniumConfiguration( myDefaultBrowser, null );
	}

}
