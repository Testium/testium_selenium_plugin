package net.sf.testium.executor.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class Negate implements ExpectedCondition<Boolean>

{
	private ExpectedCondition<Boolean> condition;

	public Negate(ExpectedCondition<Boolean> condition)
	{
		this.condition = condition;
	}

	public Boolean apply(WebDriver webDriver)
	{
		return !condition.apply(webDriver);
	}

	public String toString()
	{
		return "check that " + condition.toString() + " is not true";
	}
}
