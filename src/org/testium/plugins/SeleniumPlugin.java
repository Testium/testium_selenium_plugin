package org.testium.plugins;

import org.testium.configuration.ConfigurationException;
import org.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.utils.RunTimeData;


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
		// Interface
		WebInterface webInterface = new WebInterface("FireFox");
		aPluginCollection.addSutInterface(webInterface);
	}
}
