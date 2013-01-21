package net.sf.testium.configuration;

import java.util.ArrayList;

import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg 
 * 
 *  <SeleniumInterfaces>
 *    <SeleniumInterface>...</SeleniumInterface>
 *    <SeleniumInterface>...</SeleniumInterface>
 *    <SeleniumInterface>...</SeleniumInterface>
 *  ...
 *  </SeleniumInterfaces>
 * 
 */
public class SeleniumInterfacesXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "SeleniumInterfaces";

	private static final String SELENIUM_INTERFACE_ELEMENT 	= "SeleniumInterface";
	
	private ArrayList<String> interfaceNames;

	private GenericTagAndStringXmlHandler myInterfaceXmlHandler;

	public SeleniumInterfacesXmlHandler( XMLReader anXmlReader )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    myInterfaceXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, SELENIUM_INTERFACE_ELEMENT);
		this.addElementHandler(myInterfaceXmlHandler);

	    reset();
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
		    
		if (aQualifiedName.equalsIgnoreCase(SELENIUM_INTERFACE_ELEMENT))
    	{
			String interfaceName = myInterfaceXmlHandler.getValue();
			interfaceNames.add(interfaceName);

			myInterfaceXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}

	public ArrayList<String> getInterfaceNames() {
		return interfaceNames;
	}

	public void reset()
	{
		interfaceNames = new ArrayList<String>();
	}

}
