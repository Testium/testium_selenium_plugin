package org.testium.configuration;

import java.util.Hashtable;

import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


public class SeleniumConfigurationXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "SeleniumConfiguration";

	private SeleniumInterfaceXmlHandler myInterfaceXmlHandler;
	private Hashtable<String, SeleniumConfiguration> myConfigurations = null;

	public SeleniumConfigurationXmlHandler(XMLReader anXmlReader, RunTimeData anRtData)
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    myConfigurations = new Hashtable<String, SeleniumConfiguration>();

	    myInterfaceXmlHandler = new SeleniumInterfaceXmlHandler(anXmlReader, anRtData);
		this.addStartElementHandler(SeleniumInterfaceXmlHandler.START_ELEMENT, myInterfaceXmlHandler);
		myInterfaceXmlHandler.addEndElementHandler(SeleniumInterfaceXmlHandler.START_ELEMENT, this);
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
		    
		if (aQualifiedName.equalsIgnoreCase( myInterfaceXmlHandler.getStartElement() ))
    	{
			SeleniumConfiguration configuration = myInterfaceXmlHandler.getConfiguration();
			myConfigurations.put(configuration.getInterfaceName(), configuration );
			myInterfaceXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}
	
	public Hashtable<String, SeleniumConfiguration> getConfigurations()
	{
		return myConfigurations;
	}
}
