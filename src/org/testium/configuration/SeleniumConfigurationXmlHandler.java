package org.testium.configuration;

import java.io.File;

import org.testium.Testium;
import org.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import org.testium.executor.SupportedInterfaceList;
import org.testium.executor.TestStepMetaExecutor;
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
 *    <Interfaces>...</Interfaces>
 *  ...
 *  </SeleniumConfiguration>
 * 
 */

public class SeleniumConfigurationXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "SeleniumConfiguration";

	private static final String DEF_BROWSER_ELEMENT = "DefaultBrowser";
	private static final String SELENIUM_LIBS_DIR_ELEMENT = "SeleniumLibsDir";

	private GenericTagAndStringXmlHandler myDefaultBrowserXmlHandler;
	private GenericTagAndStringXmlHandler mySeleniumLibsDirXmlHandler;
	private SeleniumInterfacesXmlHandler myInterfacesXmlHandler;
	
	private BROWSER_TYPE myDefaultBrowser = BROWSER_TYPE.HTMLUNIT;
	private File mySeleniumLibsDir;

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
		myRtData = anRtData;
		
	    myDefaultBrowserXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, DEF_BROWSER_ELEMENT);
		this.addElementHandler(DEF_BROWSER_ELEMENT, myDefaultBrowserXmlHandler);

		mySeleniumLibsDirXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SELENIUM_LIBS_DIR_ELEMENT);
		this.addElementHandler(SELENIUM_LIBS_DIR_ELEMENT, mySeleniumLibsDirXmlHandler);

		myInterfacesXmlHandler = new SeleniumInterfacesXmlHandler(anXmlReader, anInterfaceList, aTestStepMetaExecutor);
		this.addElementHandler(SeleniumInterfacesXmlHandler.START_ELEMENT, myInterfacesXmlHandler);
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
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
		    	      aQualifiedName + " )", true);
		    
		if (aQualifiedName.equalsIgnoreCase( DEF_BROWSER_ELEMENT ))
    	{
			myDefaultBrowser = BROWSER_TYPE.valueOf( BROWSER_TYPE.class, myDefaultBrowserXmlHandler.getValue() );
			myInterfacesXmlHandler.setDefaultBrowser( myDefaultBrowser );

			myDefaultBrowserXmlHandler.reset();	
    	}
		else if (aQualifiedName.equalsIgnoreCase( SELENIUM_LIBS_DIR_ELEMENT ))
    	{
			String SeleniumLibsDirName = mySeleniumLibsDirXmlHandler.getValue();
			SeleniumLibsDirName = myRtData.substituteVars(SeleniumLibsDirName);
			mySeleniumLibsDir = new File( SeleniumLibsDirName );

			mySeleniumLibsDirXmlHandler.reset();	
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
		return new SeleniumConfiguration( myDefaultBrowser, mySeleniumLibsDir );
	}
}
