package org.testium.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.testium.Testium;
import org.testium.configuration.ConfigurationException;
import org.testium.configuration.SeleniumConfiguration;
import org.testium.configuration.SeleniumConfigurationXmlHandler;
import org.testium.executor.webdriver.WebInterface;
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
	public void loadPlugIn(PluginCollection aPluginCollection,
			RunTimeData anRtData) throws ConfigurationException
	{
		File pluginsDir = anRtData.getValueAsFile(Testium.PLUGINSDIR);
		File seleniumLibs = new File( pluginsDir, "SeleniumLibs" );
		try
		{
			PluginClassLoader.addDirToClassLoader( seleniumLibs );
		}
		catch (MalformedURLException e)
		{
			throw new ConfigurationException( e );
		}

		// Interface
		Hashtable<String, SeleniumConfiguration> configs = readConfigFiles( anRtData );
		for (Enumeration<String> ids = configs.keys(); ids.hasMoreElements();)
		{
			WebInterface webInterface = new WebInterface( configs.get( ids.nextElement() ) );
			aPluginCollection.addSutInterface(webInterface);
		}
	}

	public Hashtable<String, SeleniumConfiguration> readConfigFiles( RunTimeData anRtData ) throws ConfigurationException
	{
		Trace.println(Trace.UTIL);

		File configDir = (File) anRtData.getValue(Testium.CONFIGDIR);
		File configFile = new File( configDir, "selenium.xml" );
		Hashtable<String, SeleniumConfiguration> configs = readConfigFile( configFile, anRtData );
		
		File userConfigDir = (File) anRtData.getValue(Testium.USERCONFIGDIR);
		File userConfigFile = new File( userConfigDir, "selenium.xml" );
		Hashtable<String, SeleniumConfiguration> userConfigs = new Hashtable<String, SeleniumConfiguration>();
		if ( userConfigFile.exists() )
		{
			userConfigs = readConfigFile( userConfigFile, anRtData );
		}

		for (Enumeration<String> ids = userConfigs.keys(); ids.hasMoreElements();)
		{
			String ifaceName = ids.nextElement();
			configs.put( ifaceName, userConfigs.get(ifaceName) );
		}

		return configs;
	}
	
	public Hashtable<String, SeleniumConfiguration> readConfigFile( File aConfigFile, RunTimeData anRtData ) throws ConfigurationException
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
			handler = new SeleniumConfigurationXmlHandler(xmlReader, anRtData);

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
		
		Hashtable<String, SeleniumConfiguration> myConfigurations = handler.getConfigurations();
		
		return myConfigurations;
	}
}
