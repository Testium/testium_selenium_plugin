package org.testium.configuration;
/**
 * 
 */

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class SeleniumConfiguration
{
	public static enum BROWSER_TYPE
	{
		FIREFOX,
		CHROME,
		HTMLUNIT,
//		IPHONE,
		IE;
	}

	public final static BROWSER_TYPE FIREFOX	= BROWSER_TYPE.FIREFOX;
	public final static BROWSER_TYPE CHROME		= BROWSER_TYPE.CHROME;
	public final static BROWSER_TYPE HTMLUNIT	= BROWSER_TYPE.HTMLUNIT;
//	public final static BROWSER_TYPE IPHONE		= BROWSER_TYPE.IPHONE;
	public final static BROWSER_TYPE IE			= BROWSER_TYPE.IE;

	private String myInterfaceName;
	private BROWSER_TYPE myBrowser;

	/**
	 * @param anInterfaceName
	 * @param aBrowser
	 */
	public SeleniumConfiguration( String anInterfaceName,
	                              BROWSER_TYPE aBrowser )
	{
	    Trace.println(Trace.CONSTRUCTOR);

	    myInterfaceName = anInterfaceName;
	    myBrowser = aBrowser;
	}

	/**
	 * @return the Interface Name
	 */
	public String getInterfaceName()
	{
		return myInterfaceName;
	}

	/**
	 * @return the Browser type
	 */
	public BROWSER_TYPE getBrowserType()
	{
		return myBrowser;
	}
}
