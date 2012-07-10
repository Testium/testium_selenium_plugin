package net.sf.testium.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
//import java.util.Enumeration;
//import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.testium.Testium;
import net.sf.testium.configuration.ConfigurationException;
import net.sf.testium.configuration.SeleniumConfiguration;
import net.sf.testium.configuration.SeleniumConfigurationXmlHandler;
import net.sf.testium.executor.SupportedInterfaceList;
import net.sf.testium.executor.TestStepMetaExecutor;
//import net.sf.testium.executor.webdriver.WebInterface;
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
	
	public void loadPlugIn(
	                        PluginCollection aPluginCollection,
	                        RunTimeData anRtData
	                      ) throws ConfigurationException
	{
		// Interfaces
		SupportedInterfaceList interfaceList = aPluginCollection.getInterfaces();
		TestStepMetaExecutor testStepMetaExecutor = aPluginCollection.getTestStepExecutor();
//		SeleniumConfiguration config = readConfigFile( anRtData, interfaceList, testStepMetaExecutor );
//		File seleniumLibsDir = config.getSeleniumLibsDir();

// WORKAROUND
// The interfaces can only be created when the seleniumlibs are loaded, but the interfaces are created
// when configuration is read.
// TODO possible solutions:
// 1) Get seleniumLibsDir first from configuration
// 2) Load seleniumLibs as soon as seleniumLibsDir is read (hence inside SeleniumConfigurationXmlHandler)
		
		File pluginsDir = anRtData.getValueAsFile( Testium.PLUGINSDIR );	
		File seleniumLibsDir = new File( pluginsDir, "SeleniumLibs" );

		try
		{
			PluginClassLoader.addDirToClassLoader( seleniumLibsDir );
		}
		catch (MalformedURLException e)
		{
			throw new ConfigurationException( e );
		}

		readConfigFile( anRtData, interfaceList, testStepMetaExecutor );

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
		
		File userConfigDir = (File) anRtData.getValue(Testium.USERCONFIGDIR);
		File userConfigFile = new File( userConfigDir, "selenium.xml" );
//		SeleniumConfiguration userConfig;
		if ( userConfigFile.exists() )
		{
//			userConfig = 
			readConfigFile( anRtData, userConfigFile, anInterfaceList, aTestStepMetaExecutor );
		}

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
