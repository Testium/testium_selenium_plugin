package net.sf.testium.configuration;
/**
 * 
 */

import java.net.URL;
import java.util.ArrayList;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;

import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class SeleniumInterfaceConfiguration
{
	public enum SAVE_SOURCE {
		NEVER,
		ONFAIL,
		ALWAYS;
		
		public String toString()
		{
			return super.toString().toLowerCase();
		}

		//overriding valueOf gives a compile error
		public static SAVE_SOURCE enumOf(String strValue) throws TestSuiteException
		{
			SAVE_SOURCE value = SAVE_SOURCE.NEVER;
			if ( strValue == null ) {
				throw new TestSuiteException( "String is null. Allowed values are " + valuesString() );
			}
			else if ( strValue.isEmpty() )	{
				throw new TestSuiteException( "String is empty. Allowed values are " + valuesString() );
			} else {
				try	{
					value = Enum.valueOf( SAVE_SOURCE.class, strValue.toUpperCase() );
				}
				catch( IllegalArgumentException iae ) {
					throw new TestSuiteException( "\"" + strValue + "\" is not allowed. Only " + valuesString() );
				}
			}

			return value;
		}

		public static String valuesString() {
			String allValues = "";
			for( SAVE_SOURCE supportedValues : SAVE_SOURCE.values() ) {
				allValues = (allValues.isEmpty() ? supportedValues.toString() : allValues + ", " + supportedValues);
			}
			return allValues;
		}
	}

	private String interfaceName;
	private BROWSER_TYPE browserType;
	private URL seleniumGridUrl;
	private String baseUrl;
	private SAVE_SOURCE saveScreenShot = SAVE_SOURCE.NEVER;
	private SAVE_SOURCE savePageSource = SAVE_SOURCE.NEVER;
	private ArrayList<String> customKeywordLinks;

//	/**
//	 * @param anInterfaceName 
//	 * @param aBrowserType
//	 */
//	public SeleniumInterfaceConfiguration( 
//	                              String anInterfaceName,
//	                              BROWSER_TYPE aBrowserType )
//	{
//	    Trace.println(Trace.CONSTRUCTOR);
//
//	    this.interfaceName = anInterfaceName;
//	    this.browserType = aBrowserType;
//	    this.setBaseUrl("");
//	    this.setCustomKeywordLinks(new ArrayList<String>());
//	}
//
	public SeleniumInterfaceConfiguration(
			String anInterfaceName,
			BROWSER_TYPE aBrowserType,
			URL aSeleniumGridUrl,
			String aBaseUrl, SAVE_SOURCE aSavePageSource, SAVE_SOURCE aSaveScreenShot,
			ArrayList<String> aCustomKeywordLinks) {
	    Trace.println(Trace.CONSTRUCTOR);

	    this.interfaceName = anInterfaceName;
	    this.browserType = aBrowserType;
	    this.seleniumGridUrl = aSeleniumGridUrl;
	    this.setBaseUrl(aBaseUrl);
	    this.savePageSource = aSavePageSource;
	    this.saveScreenShot = aSaveScreenShot;
	    this.setCustomKeywordLinks(aCustomKeywordLinks);
	}

	/**
	 * @return the Interface Name
	 * 
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * @return the Browser type
	 */
	public BROWSER_TYPE getBrowserType()
	{
		return browserType;
	}

	/**
	 * @param type the browser-type to set
	 */
	public void setType(BROWSER_TYPE type) {
		this.browserType = type;
	}

	/**
	 * @return the saveScreenShot
	 */
	public SAVE_SOURCE getSaveScreenShot() {
		return saveScreenShot;
	}

	/**
	 * @param saveScreenShot the saveScreenShot to set
	 */
	public void setSaveScreenShot(SAVE_SOURCE saveScreenShot) {
		this.saveScreenShot = saveScreenShot;
	}

	/**
	 * @return the savePageSource
	 */
	public SAVE_SOURCE getSavePageSource() {
		return savePageSource;
	}

	/**
	 * @param savePageSource the savePageSource to set
	 */
	public void setSavePageSource(SAVE_SOURCE savePageSource) {
		this.savePageSource = savePageSource;
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the customKeywordLinks
	 */
	public ArrayList<String> getCustomKeywordLinks() {
		return customKeywordLinks;
	}

	/**
	 * @param customKeywordLinks the customKeywordLinks to set
	 */
	public void setCustomKeywordLinks(ArrayList<String> customKeywordLinks) {
		this.customKeywordLinks = customKeywordLinks;
	}

	/**
	 * @return the seleniumGridUrl
	 */
	public URL getSeleniumGridUrl() {
		return seleniumGridUrl;
	}

	/**
	 * @param seleniumGridUrl the seleniumGridUrl to set
	 */
	public void setSeleniumGridUrl(URL seleniumGridUrl) {
		this.seleniumGridUrl = seleniumGridUrl;		
	}
}
