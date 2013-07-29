package net.sf.testium.executor.webdriver;

import junit.framework.TestCase;

import org.junit.Test;
import org.testtoolinterfaces.testsuite.TestSuiteException;

import net.sf.testium.configuration.SeleniumConfiguration.BROWSER_TYPE;

public class BrowserTypeTester extends TestCase 
{
	@Test
	public void testcase_BrowserType_null()
	{
		BROWSER_TYPE type;
		try {
			type = BROWSER_TYPE.enumOf(null);
			assertEquals(BROWSER_TYPE.HTMLUNIT.toString(), type.toString());
		} catch (TestSuiteException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testcase_BrowserType_empty()
	{
		BROWSER_TYPE type;
		try {
			type = BROWSER_TYPE.enumOf("");
			assertEquals(BROWSER_TYPE.HTMLUNIT.toString(), type.toString());
		} catch (TestSuiteException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testcase_BrowserType_invalid()
	{
		BROWSER_TYPE type;
		try {
			type = BROWSER_TYPE.enumOf("invalid");
			assertEquals(BROWSER_TYPE.HTMLUNIT.toString(), type.toString());
		} catch (TestSuiteException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testcase_BrowserType_firefox()
	{
		BROWSER_TYPE type;
		try {
			type = BROWSER_TYPE.enumOf("firefox");
			assertEquals(BROWSER_TYPE.FIREFOX.toString(), type.toString());
		} catch (TestSuiteException e) {
			fail(e.getLocalizedMessage());
		}
	}
}
