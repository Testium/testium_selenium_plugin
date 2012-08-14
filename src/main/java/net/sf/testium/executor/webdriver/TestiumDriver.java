package net.sf.testium.executor.webdriver;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;

public interface TestiumDriver {

	public BROWSER_TYPE getType();
}
