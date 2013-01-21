package net.sf.testium.configuration;

import java.util.ArrayList;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;

import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.TTIException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg 
 * 
 *  <SeleniumInterface>
 *    <BrowserType>...</BrowserType>
 *    <BaseUrl>...</BaseUrl>
 *    <CustomStepDefinitionsLink>...</CustomStepDefinitionsLink>
 *  ...
 *  </SeleniumInterface>
 * 
 */
public class SeleniumInterfaceXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT 			= "SeleniumInterface";

	private static final String	BROWSER_TYPE_ELEMENT 	= "BrowserType";
	private static final String BASE_URL_ELEMENT 		= "BaseUrl";
	private static final String CUSTOMSTEP_DEFINITIONS_LINK_ELEMENT = "CustomStepDefinitionsLink";

	private GenericTagAndStringXmlHandler myBaseUrlXmlHandler;
	private GenericTagAndStringXmlHandler myBroserTypeXmlHandler;
	private GenericTagAndStringXmlHandler myCustomStepDefinitionsLinkXmlHandler;
	private GenericTagAndStringXmlHandler mySavePageSourceXmlHandler;
	private GenericTagAndStringXmlHandler mySaveScreenShotXmlHandler;

	private String myType;
	private String myBaseUrl;
	private String mySavePageSource;
	private String mySaveScreenShot; 
	private ArrayList<String> myCustomKeywordLinks;
	
	public SeleniumInterfaceXmlHandler( XMLReader anXmlReader )	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    this.reset();
	    
		myBaseUrlXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, BASE_URL_ELEMENT);
		this.addElementHandler(myBaseUrlXmlHandler);

		myBroserTypeXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, BROWSER_TYPE_ELEMENT);
		this.addElementHandler(myBroserTypeXmlHandler);

		myCustomStepDefinitionsLinkXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, CUSTOMSTEP_DEFINITIONS_LINK_ELEMENT);
		this.addElementHandler(myCustomStepDefinitionsLinkXmlHandler);

		mySavePageSourceXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SeleniumConfigurationXmlHandler.SAVE_PAGESOURCE);
		this.addElementHandler(mySavePageSourceXmlHandler);

		mySaveScreenShotXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SeleniumConfigurationXmlHandler.SAVE_SCREENSHOT);
		this.addElementHandler(mySaveScreenShotXmlHandler);
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
	public void processElementAttributes(String aQualifiedName, Attributes att) throws TTIException
	{
		// nop
	}

	@Override
	public void handleGoToChildElement(String aQualifiedName)
	{
		// nop
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler) throws TTIException
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
		    	      aQualifiedName + " )", true);
		    
		if (aQualifiedName.equalsIgnoreCase(BASE_URL_ELEMENT))
    	{
			myBaseUrl = myBaseUrlXmlHandler.getValue();
			myBaseUrlXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase(BROWSER_TYPE_ELEMENT))
    	{
			this.myType = myBaseUrlXmlHandler.getValue();
			myBaseUrlXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase( SeleniumConfigurationXmlHandler.SAVE_PAGESOURCE ))
    	{
			String savePageSource = mySavePageSourceXmlHandler.getValue();
			if ( !savePageSource.equalsIgnoreCase("NEVER")
					&& !savePageSource.equalsIgnoreCase("ONFAIL")
					&& !savePageSource.equalsIgnoreCase("ALWAYS") ) {
				throw new TestSuiteException( "\"" + savePageSource + "\" is not allowed for " + SeleniumConfigurationXmlHandler.SAVE_PAGESOURCE + ". Only NEVER, ONFAIL, or ALWAYS" );
			}
			
			this.mySavePageSource = savePageSource.toUpperCase();

			mySavePageSourceXmlHandler.reset();				
    	}
		else if (aQualifiedName.equalsIgnoreCase( SeleniumConfigurationXmlHandler.SAVE_SCREENSHOT ))
    	{
			String saveScreenshots = mySaveScreenShotXmlHandler.getValue();
			if ( !saveScreenshots.equalsIgnoreCase("NEVER")
					&& !saveScreenshots.equalsIgnoreCase("ONFAIL")
					&& !saveScreenshots.equalsIgnoreCase("ALWAYS") ) {
				throw new TestSuiteException( "\"" + saveScreenshots + "\" is not allowed for " + SeleniumConfigurationXmlHandler.SAVE_SCREENSHOT + ". Only NEVER, ONFAIL, or ALWAYS" );
			}
			this.mySaveScreenShot = saveScreenshots.toUpperCase();
			
			mySaveScreenShotXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( CUSTOMSTEP_DEFINITIONS_LINK_ELEMENT ))
    	{
			String fileName = myCustomStepDefinitionsLinkXmlHandler.getValue();
			myCustomKeywordLinks.add(fileName);
			myCustomStepDefinitionsLinkXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}
	
	public SeleniumInterfaceConfiguration getConfiguration( SeleniumInterfaceConfiguration config ) {
		
		if ( ! myType.isEmpty() ) {
			config.setType( BROWSER_TYPE.enumOf(myType) );
		}
		
		if ( ! myBaseUrl.isEmpty() ) {
			config.setBaseUrl(myBaseUrl);
		}
		
		if ( ! mySavePageSource.isEmpty() ) {
			config.setSavePageSource(new Boolean(mySavePageSource));
		}
		
		if ( ! mySaveScreenShot.isEmpty() ) {
			config.setSavePageSource(new Boolean(mySaveScreenShot));
		}
		
		if ( ! myCustomKeywordLinks.isEmpty() ) {
			config.setCustomKeywordLinks(myCustomKeywordLinks);
		}
		
		return config;
	}
	
	public void reset()
	{
	    myType = "";
	    myBaseUrl = "";
	    mySavePageSource = "";
	    mySaveScreenShot = "";
	    myCustomKeywordLinks = new ArrayList<String>();
	}
}
