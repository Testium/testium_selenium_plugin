package org.testium.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
//import java.util.Enumeration;
//import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testium.Testium;
import org.testium.configuration.ConfigurationException;
import org.testium.configuration.SeleniumConfiguration;
import org.testium.configuration.SeleniumConfigurationXmlHandler;
import org.testium.executor.SupportedInterfaceList;
import org.testium.executor.TestStepMetaExecutor;
//import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Arjan Kranenburg
 *
 */
public final class SeleniumPlugin implements Plugin
{
	public SeleniumPlugin()
	{
		// nop
	}
	
	@Override
	public void loadPlugIn(
	                        PluginCollection aPluginCollection,
	                        RunTimeData anRtData
	                      ) throws ConfigurationException
	{
		// Interfaces
		SupportedInterfaceList interfaceList = aPluginCollection.getInterfaces();
		TestStepMetaExecutor testStepMetaExecutor = aPluginCollection.getTestStepExecutor();
		SeleniumConfiguration config = readConfigFile( anRtData, interfaceList, testStepMetaExecutor );
		File seleniumLibsDir = config.getSeleniumLibsDir();

		try
		{
			PluginClassLoader.addDirToClassLoader( seleniumLibsDir );
		}
		catch (MalformedURLException e)
		{
			throw new ConfigurationException( e );
		}

//		Hashtable<String, SeleniumConfiguration> configs = readConfigFiles( anRtData, interfaceList );
//		for (Enumeration<String> ids = configs.keys(); ids.hasMoreElements();)
//		{
//			WebInterface webInterface = new WebInterface( configs.get( ids.nextElement() ) );
//			aPluginCollection.addSutInterface(webInterface);
//		}
	}

	public SeleniumConfiguration readConfigFile(
	                                                 RunTimeData anRtData,
	                                                 SupportedInterfaceList anInterfaceList,
	                                                 TestStepMetaExecutor aTestStepMetaExecutor
	                                            )    throws ConfigurationException
	{
		Trace.println(Trace.UTIL);

		File configDir = (File) anRtData.getValue(Testium.CONFIGDIR);
		File configFile = new File( configDir, "selenium.xml" );
		SeleniumConfiguration config = readConfigFile( anRtData, configFile, anInterfaceList, aTestStepMetaExecutor );
		
//		File userConfigDir = (File) anRtData.getValue(Testium.USERCONFIGDIR);
//		File userConfigFile = new File( userConfigDir, "selenium.xml" );
//		SeleniumConfiguration userConfig;
//		if ( userConfigFile.exists() )
//		{
//			userConfig = readConfigFile( userConfigFile, anInterfaceList );
//		}

//		for (Enumeration<String> ids = userConfigs.keys(); ids.hasMoreElements();)
//		{
//			String ifaceName = ids.nextElement();
//			configs.put( ifaceName, userConfigs.get(ifaceName) );
//		}

		return config;
	}
	
	public SeleniumConfiguration readConfigFile(    RunTimeData anRtData,
	                                                File aConfigFile,
	                                                SupportedInterfaceList anInterfaceList,
	                                                TestStepMetaExecutor aTestStepMetaExecutor
	                                           )    throws ConfigurationException
	{
		Trace.println(Trace.UTIL, "readConfigFile( " + aConfigFile.getName() + " )", true );
        // create a parser
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(false);
        SAXParser saxParser;
        SeleniumConfigurationXmlHandler handler = null;
		try
		{
			saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();

	        // create a handler
			handler = new SeleniumConfigurationXmlHandler(xmlReader, anInterfaceList, aTestStepMetaExecutor, anRtData);

	        // assign the handler to the parser
	        xmlReader.setContentHandler(handler);

	        // parse the document
	        xmlReader.parse( aConfigFile.getAbsolutePath() );
		}
		catch (ParserConfigurationException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		catch (SAXException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}
		catch (IOException e)
		{
			Trace.print(Trace.UTIL, e);
			throw new ConfigurationException( e );
		}

		SeleniumConfiguration configuration = handler.getConfiguration();
		
		return configuration;
	}
}
