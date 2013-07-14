package net.sf.testium.plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;

import net.sf.testium.Testium;
import net.sf.testium.configuration.ConfigurationException;
import net.sf.testium.configuration.CustomStepDefinitionsXmlHandler;
import net.sf.testium.configuration.PersonalSeleniumConfigurationXmlHandler;
import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfigurationXmlHandler;
import net.sf.testium.configuration.SeleniumInterfaceConfiguration;
import net.sf.testium.configuration.SeleniumInterfaceXmlHandler;
import net.sf.testium.executor.CustomInterface;
import net.sf.testium.executor.DefaultInterface;
import net.sf.testium.executor.SupportedInterfaceList;
import net.sf.testium.executor.TestStepMetaExecutor;
import net.sf.testium.executor.webdriver.WebInterface;
import net.sf.testium.executor.webdriver.commands.CheckListSize_modified;
import net.sf.testium.executor.webdriver.commands.GetListItem_modified;
import net.sf.testium.executor.webdriver.commands.GetListSize_modified;

import org.testtoolinterfaces.testsuite.TestInterfaceList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.TTIException;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg
 *
 */
public class SeleniumPlugin implements Plugin
{
	public static final String BASEURL = "BaseUrl";

	public SeleniumPlugin()
	{
		// nop
	}
	
	public void loadPlugIn(
	                        PluginCollection aPluginCollection,
	                        RunTimeData anRtData
	                      ) throws ConfigurationException
	{
		// Interfaces
		SupportedInterfaceList interfaceList = aPluginCollection.getInterfaces();
		TestStepMetaExecutor testStepMetaExecutor = aPluginCollection.getTestStepExecutor();
		SeleniumConfiguration config = readConfigFile( anRtData );
		File seleniumLibsDir = config.getSeleniumLibsDir();

		try
		{
			PluginClassLoader.addDirToClassLoader( seleniumLibsDir );
		}
		catch (MalformedURLException e)
		{
			throw new ConfigurationException( e );
		}

		DefaultInterface defInterface = (DefaultInterface) interfaceList.getInterface(DefaultInterface.NAME);
		defInterface.add( new CheckListSize_modified( defInterface ) );
		defInterface.add( new GetListItem_modified( defInterface ) );
		defInterface.add( new GetListSize_modified( defInterface ) );

		createInterfaces(anRtData, interfaceList, testStepMetaExecutor, config);
	}

	/**
	 * @param anRtData
	 * @param anInterfaceList
	 * @param aTestStepMetaExecutor
	 * @param aConfig
	 * @throws ConfigurationException
	 */
	private void createInterfaces(RunTimeData anRtData,
			SupportedInterfaceList anInterfaceList,
			TestStepMetaExecutor aTestStepMetaExecutor,
			SeleniumConfiguration aConfig) throws ConfigurationException {
		Iterator<String> interfaceNamesItr = aConfig.getInterfaceNames().iterator(); 
		while ( interfaceNamesItr.hasNext() )
		{
			String interfaceName = interfaceNamesItr.next();
			
			createInterface(anRtData, anInterfaceList, aTestStepMetaExecutor,
					aConfig, interfaceName);
		}
	}

	/**
	 * @param anRtData
	 * @param anInterfaceList
	 * @param aTestStepMetaExecutor
	 * @param aConfig
	 * @param anInterfaceName
	 * @throws ConfigurationException
	 */
	private void createInterface(RunTimeData anRtData,
			SupportedInterfaceList anInterfaceList,
			TestStepMetaExecutor aTestStepMetaExecutor,
			SeleniumConfiguration aConfig, String anInterfaceName)
			throws ConfigurationException {

		File configDir = (File) anRtData.getValue(Testium.CONFIGDIR);
		File interfaceConfigurationFile = new File( configDir, anInterfaceName + ".xml" );
//		SeleniumInterfaceConfiguration tmpIfConfig = new SeleniumInterfaceConfiguration(anInterfaceName, aConfig.getBrowserType());
		SeleniumInterfaceConfiguration globalIfConfig = 
//				readInterfaceConfiguration( interfaceDefinitionsFile, aConfig, tmpIfConfig );
				readGloaInterfaceConfiguration( interfaceConfigurationFile, anInterfaceName, aConfig );

		File userConfigDir = (File) anRtData.getValue(Testium.USERCONFIGDIR);
		File personalInterfaceConfigurationFile = new File( userConfigDir, anInterfaceName + ".xml" );
		SeleniumInterfaceConfiguration ifConfig = globalIfConfig;
		if ( personalInterfaceConfigurationFile.canRead() ) {
			ifConfig = readPersonalInterfaceConfiguration( personalInterfaceConfigurationFile, anInterfaceName, globalIfConfig );
		}

//		ifConfig.setSeleniumGridUrl( aConfig.getSeleniumGridUrl() );
		
		String sysPropBaseUrl = System.getProperty( anInterfaceName + "." + SeleniumPlugin.BASEURL );
		if ( sysPropBaseUrl != null ) {
			ifConfig.setBaseUrl( sysPropBaseUrl );
		}
		WebInterface iface = new WebInterface( anInterfaceName, anRtData, ifConfig );
		anInterfaceList.add(iface);

		createCustomKeywords(anRtData, anInterfaceList, aTestStepMetaExecutor,
				ifConfig, iface);
	}

	/**
	 * @param anRtData
	 * @param interfaceList
	 * @param testStepMetaExecutor
	 * @param ifConfig
	 * @param iface
	 * @throws ConfigurationException
	 */
	private void createCustomKeywords(RunTimeData anRtData,
			SupportedInterfaceList interfaceList,
			TestStepMetaExecutor testStepMetaExecutor,
			SeleniumInterfaceConfiguration ifConfig, WebInterface iface)
			throws ConfigurationException {
		Iterator<String> keywordsDefLinksItr = ifConfig.getCustomKeywordLinks().iterator(); 
		while ( keywordsDefLinksItr.hasNext() )
		{
			String keywordsDefLink = keywordsDefLinksItr.next();

			String fileName = anRtData.substituteVars(keywordsDefLink);
			CustomStepDefinitionsXmlHandler.loadElementDefinitions( new File( fileName ),
						anRtData, iface, interfaceList, testStepMetaExecutor );
		}
	}

//	private SeleniumInterfaceConfiguration readInterfaceConfiguration(
//			File interfaceDefinitionsFile,
//			SeleniumConfiguration selConfig,
//			SeleniumInterfaceConfiguration selIfConfig
//			) throws ConfigurationException {
//		Trace.println(Trace.UTIL, "readInterfaceConfiguration( " + selIfConfig.getInterfaceName() + " )", true );
//
//		SeleniumInterfaceXmlHandler handler = null;
//		try {
//			XMLReader reader = XmlHandler.getNewXmlReader();
//			handler = new SeleniumInterfaceXmlHandler( reader, selConfig );
//		
//			handler.parse(reader, interfaceDefinitionsFile);
//		} catch (TTIException e) {
//			throw new ConfigurationException( e );
//		}
//
//// TODO		tmpIfConfig.setSavePageSource( aConfig.getSave...());
//		SeleniumInterfaceConfiguration ifConfiguration = handler.getConfiguration(selIfConfig);
//		handler.reset();
//
//		return ifConfiguration;
//	}

	private SeleniumInterfaceConfiguration readGloaInterfaceConfiguration(
			File interfaceDefinitionsFile,
			String ifName,
			SeleniumConfiguration selConfig) throws ConfigurationException {

		Trace.println(Trace.UTIL, "readInterfaceConfiguration( " + interfaceDefinitionsFile.getName()
				+ ", " + ifName + " )", true );

		SeleniumInterfaceXmlHandler handler = null;
		try {
			XMLReader reader = XmlHandler.getNewXmlReader();
			handler = new SeleniumInterfaceXmlHandler( reader, selConfig );
		
			handler.parse(reader, interfaceDefinitionsFile);
		} catch (TTIException e) {
			throw new ConfigurationException( e );
		}

		SeleniumInterfaceConfiguration ifConfiguration = handler.getConfiguration( ifName );
		handler.reset();

		return ifConfiguration;
	}

	private SeleniumInterfaceConfiguration readPersonalInterfaceConfiguration(
			File personalInterfaceConfigurationFile, String ifName,
			SeleniumInterfaceConfiguration globalIfConfig) throws ConfigurationException {
		Trace.println(Trace.UTIL, "readInterfaceConfiguration( " + personalInterfaceConfigurationFile.getName()
				+ ", " + ifName + " )", true );

		SeleniumInterfaceXmlHandler handler = null;
		try {
			XMLReader reader = XmlHandler.getNewXmlReader();
			handler = new SeleniumInterfaceXmlHandler( reader, globalIfConfig );
		
			handler.parse(reader, personalInterfaceConfigurationFile);
		} catch (TTIException e) {
			throw new ConfigurationException( e );
		}

		SeleniumInterfaceConfiguration ifConfiguration = handler.getConfiguration( ifName );
		handler.reset();

		return ifConfiguration;
	}

	public final SeleniumConfiguration readConfigFile(
			RunTimeData anRtData ) throws ConfigurationException {
		Trace.println(Trace.UTIL);

		File configDir = (File) anRtData.getValue(Testium.CONFIGDIR);
		File configFile = new File( configDir, "selenium.xml" );
		SeleniumConfiguration globalConfig = readConfigFile( anRtData, configFile );

		File userConfigDir = (File) anRtData.getValue(Testium.USERCONFIGDIR);
		File userConfigFile = new File( userConfigDir, "selenium.xml" );
		if ( userConfigFile.exists() ) {
			return readPersonalConfigFile( globalConfig, userConfigFile );
		}

		return globalConfig;
	}
	
	public final SeleniumConfiguration readConfigFile( 
			RunTimeData anRtData, File aConfigFile )    throws ConfigurationException {
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + " )", true );
		
	    SeleniumConfigurationXmlHandler myHandler;
		try {
			XMLReader reader = XmlHandler.getNewXmlReader();
			myHandler = new SeleniumConfigurationXmlHandler(reader, anRtData);
		
			myHandler.parse(reader, aConfigFile);
		} catch (TTIException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		}

		SeleniumConfiguration configuration = myHandler.getConfiguration();
		
		return configuration;
	}

	public final SeleniumConfiguration readPersonalConfigFile(
			SeleniumConfiguration globalConfig, File aConfigFile ) throws ConfigurationException {
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + " )", true );
		
		PersonalSeleniumConfigurationXmlHandler myHandler;
		try {
			XMLReader reader = XmlHandler.getNewXmlReader();
			myHandler = new PersonalSeleniumConfigurationXmlHandler(reader, globalConfig);
		
			myHandler.parse(reader, aConfigFile);
		} catch (TTIException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		}

		SeleniumConfiguration configuration = myHandler.getConfiguration();
		
		return configuration;
	}

	public static void loadElementDefinitions( File aFile, 
			   RunTimeData anRtData,
			   CustomInterface anInterface,
			   TestInterfaceList anInterfaceList,
			   TestStepMetaExecutor aTestStepMetaExecutor ) throws ConfigurationException {
		Trace.println(Trace.UTIL, "loadElementDefinitions( " + aFile.getName() + " )", true );
		
		CustomStepDefinitionsXmlHandler handler = null;
		try {
			XMLReader reader = XmlHandler.getNewXmlReader();
			handler = new CustomStepDefinitionsXmlHandler( reader, anRtData, anInterface,
					   anInterfaceList, aTestStepMetaExecutor );
		
			handler.parse(reader, aFile);
		} catch (TTIException e) {
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException(e);
		}
	}
}
