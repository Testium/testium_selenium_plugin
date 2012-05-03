package org.testium.executor.webdriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.SessionId;
import org.testium.selenium.FieldPublisher;
import org.testtoolinterfaces.testresult.TestStepResult;

public class TestiumHtmlUnitDriver extends HtmlUnitDriver implements TestiumLogger, FieldPublisher
{
	TestStepResult myTestStepResult = null;

	private final WebInterface myInterface;

	public TestiumHtmlUnitDriver( WebInterface anInterface ) {
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
