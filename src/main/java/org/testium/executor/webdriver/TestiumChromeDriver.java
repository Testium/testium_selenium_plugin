package org.testium.executor.webdriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;
import org.testium.selenium.FieldPublisher;
import org.testtoolinterfaces.testresult.TestStepResult;

public class TestiumChromeDriver extends ChromeDriver implements TestiumLogger, FieldPublisher
{
	TestStepResult myTestStepResult = null;

	private final WebInterface myInterface;
	public TestiumChromeDriver(WebInterface anInterface, ChromeOptions options)
	{
		super( options );
		myInterface = anInterface;
	}

	@SuppressWarnings("deprecation")
	public TestiumChromeDriver(WebInterface anInterface, DesiredCapabilities capabilities)
	{
		super( capabilities );
		myInterface = anInterface;
	}

	public void setTestStepResult(TestStepResult aTestStepResult)
	{
		this.myTestStepResult = aTestStepResult;
	}

	protected void log(SessionId sessionId, String commandName, Object toLog)
	{
		if ( myTestStepResult != null )
		{
			myTestStepResult.addComment(commandName);
		}
	}

	public void addElement(String varName, WebElement element) {
		myInterface.addElement(varName, element);
	}

	public WebElement getElement(String varName) {
		return myInterface.getElement(varName);
	}
}
