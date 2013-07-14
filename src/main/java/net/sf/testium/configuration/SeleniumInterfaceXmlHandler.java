package net.sf.testium.configuration;

import java.util.ArrayList;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.configuration.SeleniumInterfaceConfiguration.SAVE_SOURCE;

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

	private BROWSER_TYPE defaultType;
	private String defaultBaseUrl;
	private SAVE_SOURCE defaultSavePageSource;
	private SAVE_SOURCE defaultSaveScreenShot; 

	private BROWSER_TYPE myType;
	private String myBaseUrl;
	private SAVE_SOURCE mySavePageSource;
	private SAVE_SOURCE mySaveScreenShot; 
	private ArrayList<String> myCustomKeywordLinks;
	
	public SeleniumInterfaceXmlHandler( XMLReader anXmlReader, SeleniumConfiguration selConfig )	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    defaultType = selConfig.getBrowserType();
//	    defaultBaseUrl = selConfig.getBaseUrl();
//	    defaultBaseUrl = "";
	    defaultSavePageSource = selConfig.getSavePageSource();
	    defaultSaveScreenShot = selConfig.getSaveScreenShot();

	    genericConstructor(anXmlReader);
	}

	public SeleniumInterfaceXmlHandler(XMLReader anXmlReader,
			SeleniumInterfaceConfiguration globalIfConfig) {
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    defaultType = globalIfConfig.getBrowserType();
	    defaultBaseUrl = globalIfConfig.getBaseUrl();
	    defaultSavePageSource = globalIfConfig.getSavePageSource();
	    defaultSaveScreenShot = globalIfConfig.getSaveScreenShot();

	    genericConstructor(anXmlReader);
	}

	/**
	 * @param anXmlReader
	 */
	private void genericConstructor(XMLReader anXmlReader) {
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
			String browserTypeStr = this.myBroserTypeXmlHandler.getValue();
			myType = BROWSER_TYPE.enumOf(browserTypeStr);
			myBroserTypeXmlHandler.reset();
    	}
		else if (aQualifiedName.equalsIgnoreCase( SeleniumConfigurationXmlHandler.SAVE_PAGESOURCE ))
    	{
			String savePageSource = mySavePageSourceXmlHandler.getValue();
			this.mySavePageSource = SAVE_SOURCE.enumOf(savePageSource);
			mySavePageSourceXmlHandler.reset();				
    	}
		else if (aQualifiedName.equalsIgnoreCase( SeleniumConfigurationXmlHandler.SAVE_SCREENSHOT ))
    	{
			String saveScreenshots = mySaveScreenShotXmlHandler.getValue();
			this.mySaveScreenShot = SAVE_SOURCE.enumOf(saveScreenshots);
			
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
	
//	public SeleniumInterfaceConfiguration getConfiguration( SeleniumInterfaceConfiguration config ) {
//		
//		if ( ! myType.isEmpty() ) {
//			config.setType( BROWSER_TYPE.enumOf(myType) );
//		}
//		
//		if ( ! myBaseUrl.isEmpty() ) {
//			config.setBaseUrl(myBaseUrl);
//		}
//		
//		config.setSavePageSource(mySavePageSource);
//		config.setSavePageSource(mySaveScreenShot);
//		
//		ArrayList<String> customKeywordLinks = config.getCustomKeywordLinks();
//
//		Iterator<String> keywordsDefLinksItr = myCustomKeywordLinks.iterator(); 
//		while ( keywordsDefLinksItr.hasNext() )
//		{
//			String keywordsDefLink = keywordsDefLinksItr.next();
//			if ( ! customKeywordLinks.contains(keywordsDefLink) ) {
//				customKeywordLinks.add(keywordsDefLink);
//			}
//		}
//		config.setCustomKeywordLinks(customKeywordLinks);
//		
//		return config;
//	}
//	
	public SeleniumInterfaceConfiguration getConfiguration( String ifName ) {
		
		return new SeleniumInterfaceConfiguration(ifName, myType, myBaseUrl, mySavePageSource, mySaveScreenShot, myCustomKeywordLinks);
	}
	
	public void reset()
	{
	    myType = this.defaultType;
	    myBaseUrl = this.defaultBaseUrl;
	    mySavePageSource = this.defaultSavePageSource;
	    mySaveScreenShot = this.defaultSaveScreenShot;
	    myCustomKeywordLinks = new ArrayList<String>();
	}
}
