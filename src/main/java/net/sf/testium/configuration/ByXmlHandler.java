package net.sf.testium.configuration;

import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.By;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author Arjan Kranenburg 
 * 
 *    <By type="...">
 *    </By>
 * 
 */

public class ByXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "By";
	
	private static final String TYPE_ATTR = "type";

	private String myType;
	private String myByString;

	public ByXmlHandler( XMLReader anXmlReader )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);
		
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
		Trace.print(Trace.SUITE, "handleCharacters( " 
	            + aValue, true );
		myByString = aValue.trim();
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
     	if (aQualifiedName.equalsIgnoreCase(super.getStartElement()))
    	{
		    for (int i = 0; i < att.getLength(); i++)
		    {
	    		Trace.append( Trace.SUITE, ", " + att.getQName(i) + "=" + att.getValue(i) );
		    	if (att.getQName(i).equalsIgnoreCase(TYPE_ATTR))
		    	{
		        	myType = att.getValue(i);
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
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler) throws TestSuiteException
	{
		// nop
	}

	public By getBy( )
	{
		return WebInterface.getBy(myType, myByString);
	}

	public void reset()
	{
		myType = "";
		myByString = "";
	}
}
