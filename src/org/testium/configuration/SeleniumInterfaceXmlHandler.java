package org.testium.configuration;

import org.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


public class SeleniumInterfaceXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "interface";

	private static final String	ATTR_ID			= "id";
	private static final String	ELEMENT_TYPE	= "type";

	private String myId;
	private String myType;

	public SeleniumInterfaceXmlHandler(XMLReader anXmlReader, RunTimeData anRtData)
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    XmlHandler typeHandler = new GenericTagAndStringXmlHandler(anXmlReader, ELEMENT_TYPE);
		this.addStartElementHandler(ELEMENT_TYPE, typeHandler);
		typeHandler.addEndElementHandler(ELEMENT_TYPE, this);

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
		Trace.print(Trace.SUITE, "processElementAttributes( " 
		            + aQualifiedName, true );
	     	if (aQualifiedName.equalsIgnoreCase(START_ELEMENT))
	    	{
			    for (int i = 0; i < att.getLength(); i++)
			    {
		    		Trace.append( Trace.SUITE, ", " + att.getQName(i) + "=" + att.getValue(i) );
			    	if (att.getQName(i).equalsIgnoreCase(ATTR_ID))
			    	{
			        	myId = att.getValue(i);
			    	} // else ignore
			    	else
			    	{
						throw new Error( "The attribute '" + att.getQName(i) 
						                 + "' is not supported for configuration of the Selenium Plugin, element " + START_ELEMENT );
			    	}
			    }
	    	}
			Trace.append( Trace.SUITE, " )\n" );
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
		    
		if (aQualifiedName.equalsIgnoreCase(ELEMENT_TYPE))
    	{
			myType = aChildXmlHandler.getValue();
			aChildXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}

	/**
	 * @return the Selenium Configuration
	 */
	public SeleniumConfiguration getConfiguration()
	{
		if ( myId.isEmpty() )
		{
			throw new Error( "Attribute " + ATTR_ID + " is not defined for Selenium Interface" );
		}
		if ( myType.isEmpty() )
		{
			throw new Error( ELEMENT_TYPE + " is not defined for Interface " + myId );
		}

		BROWSER_TYPE type;
		try
		{
			type = BROWSER_TYPE.valueOf( myType );
		}
		catch ( Exception e )
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
			throw new Error( "ELEMENT_TYPE " + myType + " is not supported. Supported types are " + allTypes );
		}
		
		return new SeleniumConfiguration( myId, type );
	}


	public void reset()
	{
		myId = "";
		myType = "";
	}
}
