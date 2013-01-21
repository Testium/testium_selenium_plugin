package net.sf.testium.configuration;
/**
 * 
 */

import java.net.URL;
import java.util.ArrayList;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class SeleniumInterfaceConfiguration
{
	private String interfaceName;
	private BROWSER_TYPE browserType;
	private String baseUrl;
	private boolean saveScreenShot = false;
	private boolean savePageSource = false;
	private ArrayList<String> customKeywordLinks;
	private URL seleniumGridUrl;

	/**
	 * @param anInterfaceName 
	 * @param aBrowserType
	 */
	public SeleniumInterfaceConfiguration( 
	                              String anInterfaceName,
	                              BROWSER_TYPE aBrowserType )
	{
	    Trace.println(Trace.CONSTRUCTOR);

	    this.interfaceName = anInterfaceName;
	    this.browserType = aBrowserType;
	    this.setBaseUrl("");
	    this.setCustomKeywordLinks(new ArrayList<String>());
	}

	public SeleniumInterfaceConfiguration(
			String anInterfaceName, BROWSER_TYPE aBrowserType,
			String aBaseUrl, boolean aSavePageSource, boolean aSaveScreenShot,
			ArrayList<String> aCustomKeywordLinks) {
	    Trace.println(Trace.CONSTRUCTOR);

	    this.interfaceName = anInterfaceName;
	    this.browserType = aBrowserType;
	    this.setBaseUrl(aBaseUrl);
	    this.savePageSource = aSavePageSource;
	    this.saveScreenShot = aSaveScreenShot;
	    this.setCustomKeywordLinks(aCustomKeywordLinks);
	    
		// TODO Auto-generated constructor stub
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
	public boolean getSaveScreenShot() {
		return saveScreenShot;
	}

	/**
	 * @param saveScreenShot the saveScreenShot to set
	 */
	public void setSaveScreenShot(boolean saveScreenShot) {
		this.saveScreenShot = saveScreenShot;
	}

	/**
	 * @return the savePageSource
	 */
	public boolean getSavePageSource() {
		return savePageSource;
	}

	/**
	 * @param savePageSource the savePageSource to set
	 */
	public void setSavePageSource(boolean savePageSource) {
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
