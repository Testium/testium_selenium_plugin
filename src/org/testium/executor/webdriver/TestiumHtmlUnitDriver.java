package org.testium.executor.webdriver;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.SessionId;
import org.testtoolinterfaces.testresult.TestStepResult;

public class TestiumHtmlUnitDriver extends HtmlUnitDriver implements TestiumLogger
{
	TestStepResult myTestStepResult = null;

	@Override
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
}
