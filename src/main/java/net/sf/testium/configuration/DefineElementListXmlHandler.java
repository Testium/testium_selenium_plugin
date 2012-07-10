package net.sf.testium.configuration;

import java.util.ArrayList;

import net.sf.testium.selenium.SimpleElementList;
import net.sf.testium.selenium.WebDriverInterface;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
 *    <DefineElementList>
 *      <Name>...</Name>
 *      <By type="...">...</By>
 *      <BaseElement></BaseElement>
 *    </DefineElementList>
 * 
 */

public class DefineElementListXmlHandler extends XmlHandler
{
	private static final String START_ELEMENT = "DefineElementList";

	private static final String NAME_ELEMENT = "Name";
	private static final String BASE_ELEMENT_ELEMENT = "BaseElement";

	private ByXmlHandler myByXmlHandler;
	private GenericTagAndStringXmlHandler myNameXmlHandler;
	private GenericTagAndStringXmlHandler myBaseElementXmlHandler;

	private final RunTimeData myRtData;
	private final WebDriverInterface myInterface;
	
	private By myBy;
	private String myName;
	private WebElement myBaseElement;

	public DefineElementListXmlHandler( XMLReader anXmlReader, RunTimeData anRtData, WebDriverInterface anInterface )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

		myRtData = anRtData;
		myInterface = anInterface;
		
		myByXmlHandler = new ByXmlHandler(anXmlReader);
		this.addElementHandler(myByXmlHandler);
		
		myNameXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, NAME_ELEMENT);
		this.addElementHandler(myNameXmlHandler);
		
		myBaseElementXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, BASE_ELEMENT_ELEMENT);
		this.addElementHandler(myBaseElementXmlHandler);
		
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
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler) throws TestSuiteException
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
	    	      aQualifiedName + " )", true);
	    
		if (aQualifiedName.equalsIgnoreCase( ByXmlHandler.START_ELEMENT ))
		{
			myBy = myByXmlHandler.getBy();
			myByXmlHandler.reset();
		}
		else if (aQualifiedName.equalsIgnoreCase( NAME_ELEMENT ))
		{
			myName = myNameXmlHandler.getValue();
			myNameXmlHandler.reset();	
		}
		else if (aQualifiedName.equalsIgnoreCase( BASE_ELEMENT_ELEMENT ))
		{
			String baseElementName = myBaseElementXmlHandler.getValue();
			myBaseElement = myRtData.getValueAs(WebElement.class, baseElementName);
			myBaseElementXmlHandler.reset();	
		}
		else
		{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}

	public void defineElementList() throws ConfigurationException
	{
		if ( myName.isEmpty() ) {
			throw new ConfigurationException( "Name of element is not specified" );
		}

		if ( myBy == null ) {
			throw new ConfigurationException( "By is not defined for " + myName );
		}
		
		SimpleElementList elmList = new SimpleElementList( myBy, myInterface, new ArrayList<WebElement>(), myBaseElement );
		RunTimeVariable elementVar = new RunTimeVariable( myName, elmList );
		myRtData.add(elementVar);
	}

	public void reset()
	{
		myBy = null;
		myName = "";
		myBaseElement = null;
	}
}
