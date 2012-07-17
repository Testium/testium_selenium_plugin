package net.sf.testium.configuration;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.testium.selenium.WebDriverInterface;

import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.TTIException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Arjan Kranenburg 
 * 
 *  <ElementDefinitions>
 *    <ElementDefinitionsLink>...</ElementDefinitionsLink>
 *    <DefineElement>...</DefineElement>
 *    <DefineElementList>...</DefineElementList>
 *  </ElementDefinitions>
 * 
 */

public class LoadDefinitionsXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "ElementDefinitions";

	private static final String ELEMENT_DEFINITIONS_LINK_ELEMENT = "ElementDefinitionsLink";

	private DefineElementXmlHandler myDefineElementXmlHandler;
	private DefineElementListXmlHandler myDefineElementListXmlHandler;
	private GenericTagAndStringXmlHandler myElementDefinitionsLinkXmlHandler;

	private final RunTimeData myRtData;
	private final WebDriverInterface myInterface;
	
	public LoadDefinitionsXmlHandler( XMLReader anXmlReader, 
                                      RunTimeData anRtData,
                                      WebDriverInterface anInterface )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		myRtData = anRtData;
		myInterface = anInterface;
		
		myDefineElementXmlHandler = new DefineElementXmlHandler(anXmlReader, anRtData, anInterface);
		this.addElementHandler(myDefineElementXmlHandler);

		myDefineElementListXmlHandler = new DefineElementListXmlHandler(anXmlReader, anRtData, anInterface);
		this.addElementHandler(myDefineElementListXmlHandler);

		myElementDefinitionsLinkXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, ELEMENT_DEFINITIONS_LINK_ELEMENT);
		this.addElementHandler(myElementDefinitionsLinkXmlHandler);
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
		throws TTIException
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
		    	      aQualifiedName + " )", true);
		    
		if (aQualifiedName.equalsIgnoreCase( ELEMENT_DEFINITIONS_LINK_ELEMENT ))
    	{
			String fileName = myElementDefinitionsLinkXmlHandler.getValue();
			myElementDefinitionsLinkXmlHandler.reset();

			fileName = myRtData.substituteVars(fileName);
			try {
				LoadDefinitionsXmlHandler.loadElementDefinitions( new File( fileName ), myRtData, myInterface );
			} catch (ConfigurationException ce) {
				throw new TTIException( "Failed to load element Definitions from file: " + fileName, ce );
			}
    	} else if ( aQualifiedName.equalsIgnoreCase(myDefineElementXmlHandler.getStartElement()) )
    	{
    		try {
				myDefineElementXmlHandler.defineElement();
			} catch (ConfigurationException e) {
	    		myDefineElementXmlHandler.reset();
				throw new TTIException( "Failed to load element Definition", e );
			}
    		myDefineElementXmlHandler.reset();
    	} else if ( aQualifiedName.equalsIgnoreCase(myDefineElementListXmlHandler.getStartElement()) )
		{
			try {
				myDefineElementListXmlHandler.defineElementList();
			} catch (ConfigurationException e) {
				myDefineElementListXmlHandler.reset();
				throw new TTIException( "Failed to load elementList Definition", e );
			}
			myDefineElementListXmlHandler.reset();
		}
		// else ignore
	}

	public static void loadElementDefinitions(File aFile, RunTimeData anRtData, WebDriverInterface anInterface)
			throws ConfigurationException {
		Trace.println(Trace.UTIL, "loadElementDefinitions( " + aFile.getName()
				+ " )", true);
		// create a parser
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(false);
		SAXParser saxParser;
		try {
			saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();

			// create a handler
			LoadDefinitionsXmlHandler handler = new LoadDefinitionsXmlHandler(xmlReader, anRtData, anInterface);

			// assign the handler to the parser
			xmlReader.setContentHandler(handler);

			// parse the document
			xmlReader.parse(aFile.getAbsolutePath());
		} catch (ParserConfigurationException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		} catch (SAXException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		} catch (IOException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		}
	}
}
