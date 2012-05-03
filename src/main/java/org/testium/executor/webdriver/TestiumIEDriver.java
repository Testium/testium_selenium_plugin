package org.testium.executor.webdriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;
import org.testium.selenium.FieldPublisher;
import org.testtoolinterfaces.testresult.TestStepResult;

public class TestiumIEDriver extends InternetExplorerDriver implements TestiumLogger, FieldPublisher
{
	TestStepResult myTestStepResult = null;

	private final WebInterface myInterface;
	
	public TestiumIEDriver(WebInterface anInterface, DesiredCapabilities capabilities)
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
