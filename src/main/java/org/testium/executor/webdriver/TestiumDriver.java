package org.testium.executor.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.SessionId;
import org.testium.selenium.FieldPublisher;
import org.testium.selenium.WebDriverDecorator;
import org.testtoolinterfaces.testresult.TestStepResult;

public class TestiumDriver extends WebDriverDecorator implements TestiumLogger, FieldPublisher 
{
	private TestStepResult myTestStepResult = null;
	private final WebInterface myInterface;
	
	public TestiumDriver( WebDriver aDriver, WebInterface anInterface ) {
		super( aDriver );
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
