package net.sf.testium.configuration;

import net.sf.testium.executor.SupportedInterfaceList;
import net.sf.testium.executor.TestStepMetaExecutor;
import org.testtoolinterfaces.utils.RunTimeData;
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

	private SeleniumInterfaceXmlHandler myInterfaceXmlHandler;
	public SeleniumInterfacesXmlHandler( XMLReader anXmlReader,
	                                     SupportedInterfaceList anInterfaceList,
	                                     TestStepMetaExecutor aTestStepMetaExecutor,
	                                     RunTimeData anRtData )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    myInterfaceXmlHandler = new SeleniumInterfaceXmlHandler( anXmlReader,
	                                                             anInterfaceList,
	                                                             aTestStepMetaExecutor,
	                                                             anRtData );
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
		    
		if (aQualifiedName.equalsIgnoreCase(SeleniumInterfaceXmlHandler.START_ELEMENT))
    	{
			// The interfaceList is already updated
			myInterfaceXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}

	public void reset()
	{
		myInterfaceXmlHandler.reset();
	}

//	public void setDefaultBrowser(BROWSER_TYPE aBrowser)
//	{
//		myInterfaceXmlHandler.setDefaultBrowser( aBrowser );
//	}
}
