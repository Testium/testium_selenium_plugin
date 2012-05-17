package net.sf.testium.configuration;

//import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;
import net.sf.testium.configuration.CustomStepXmlHandler;
import net.sf.testium.executor.CustomizableInterface;
import net.sf.testium.executor.SupportedInterfaceList;
import net.sf.testium.executor.TestStepMetaExecutor;
import net.sf.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testsuite.TestInterface;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.TTIException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg 
 * 
 *  <SeleniumInterface name="..." type="...">
 *    <customstep>...</customstep>
 *    <customstep>...</customstep>
 *    <customstep>...</customstep>
 *  ...
 *  </SeleniumInterface>
 * 
 */
public class SeleniumInterfaceXmlHandler extends XmlHandler
{
	public static final String START_ELEMENT = "SeleniumInterface";

	private static final String	ATTR_NAME			= "name";
//	private static final String	ATTR_TYPE			= "type";

	private SupportedInterfaceList myInterfaceList;
	private CustomStepXmlHandler myCustomStepXmlHandler;

	private TestInterface myInterface;
	private String myInterfaceName;
//	private BROWSER_TYPE myType;
//	private BROWSER_TYPE myDefaultType = BROWSER_TYPE.HTMLUNIT;
	private final RunTimeData myRtData;
	
	public SeleniumInterfaceXmlHandler( XMLReader anXmlReader,
	                                    SupportedInterfaceList anInterfaceList,
	                                    TestStepMetaExecutor aTestStepMetaExecutor,
	                                    RunTimeData anRtData )
	{
	    super(anXmlReader, START_ELEMENT);
	    Trace.println(Trace.CONSTRUCTOR);

	    myInterfaceList = anInterfaceList;
	    myRtData = anRtData;
	    
	    myCustomStepXmlHandler = new CustomStepXmlHandler(anXmlReader, anInterfaceList, aTestStepMetaExecutor);
		this.addElementHandler(CustomStepXmlHandler.START_ELEMENT, myCustomStepXmlHandler);

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
	    Trace.println(Trace.UTIL, "handleEndElement( " + 
		    	      aQualifiedName + " )", true);
		if ( myInterface == null )
		{
			// We now have the rare case that the interface is defined, but no custom steps were defined.
			createInterface();
		}
	}

	@Override
	public void processElementAttributes(String aQualifiedName, Attributes att) throws TTIException
	{
		Trace.print(Trace.SUITE, "processElementAttributes( " 
		            + aQualifiedName, true );
	     	if (aQualifiedName.equalsIgnoreCase(START_ELEMENT))
	    	{
			    for (int i = 0; i < att.getLength(); i++)
			    {
		    		Trace.append( Trace.SUITE, ", " + att.getQName(i) + "=" + att.getValue(i) );
			    	if (att.getQName(i).equalsIgnoreCase(ATTR_NAME))
			    	{
			    		myInterfaceName = att.getValue(i);
			    	}
//			    	else if (att.getQName(i).equalsIgnoreCase(ATTR_TYPE))
//			    	{
//			        	String type = att.getValue(i);
//			    		try
//			    		{
//			    			myType = BROWSER_TYPE.valueOf( BROWSER_TYPE.class, type );
//			    		}
//			    		catch ( Exception e )
//			    		{
//			    			String allTypes = "";
//			    			for( BROWSER_TYPE supportedType : BROWSER_TYPE.values() )
//			    			{
//			    				if (allTypes.isEmpty())
//			    				{
//			    					allTypes = supportedType.toString();
//			    				}
//			    				else
//			    				{
//			    					allTypes += ", " + supportedType;
//			    				}
//			    			}
//			    			throw new TTIException( "ELEMENT_TYPE " + type + " is not supported. Supported types are " + allTypes );
//			    		}
//			    	} // else ignore
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
	    Trace.println(Trace.UTIL, "handleGoToChildElement( " + 
		    	      aQualifiedName + " )", true);
		if ( myInterface == null )
		{
			createInterface();
		}
	}

	@Override
	public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler) throws TTIException
	{
	    Trace.println(Trace.UTIL, "handleReturnFromChildElement( " + 
		    	      aQualifiedName + " )", true);
		    
		if (aQualifiedName.equalsIgnoreCase(CustomStepXmlHandler.START_ELEMENT))
    	{
    		if ( myInterface == null )
    		{
    			throw new TTIException( "The interface is not defined. Unable to add a step to an unknown interface" );
    		}
    		
    		if ( ! CustomizableInterface.class.isInstance(myInterface) )
    		{
    			throw new TTIException( "The " + myInterface.getInterfaceName() + " interface is not customizable. "
    			                        + "Unable to add a step to it." );
    		}

			try
			{
				myCustomStepXmlHandler.addTestStepExecutor( (CustomizableInterface) myInterface );
			}
			catch (TestSuiteException e)
			{
    			throw new TTIException( "Unable to add a step: " + e.getMessage(), e );
			}
			myCustomStepXmlHandler.reset();
    	}
		else
    	{ // Programming fault
			throw new Error( "Child XML Handler returned, but not recognized. The handler was probably defined " +
			                 "in the Constructor but not handled in handleReturnFromChildElement()");
		}
	}
	
	private void createInterface()
	{
		if ( myInterfaceName.isEmpty() )
		{
			throw new Error( "Attribute " + ATTR_NAME + " is not defined for Selenium Interface" );
		}
		myInterface = myInterfaceList.getInterface(myInterfaceName);
		if ( myInterface == null )
		{
			// Create new interface
//			myInterface = new WebInterface( myInterfaceName, myType );
			myInterface = new WebInterface( myInterfaceName, myRtData );
			myInterfaceList.add(myInterface);
		}
	}

	public void reset()
	{
		myInterface = null;
		myInterfaceName = "";
//		myType = myDefaultType;
	}

//	public void setDefaultBrowser(BROWSER_TYPE aBrowser)
//	{
//		myDefaultType = aBrowser;
//	}
}
