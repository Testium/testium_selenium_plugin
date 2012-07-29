package net.sf.testium.executor.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


public class NotNull<T> implements ExpectedCondition<Boolean>
{
	private ExpectedCondition<T> condition;

	public NotNull(ExpectedCondition<T> condition)
	{
		this.condition = condition;
	}

	public Boolean apply(WebDriver webDriver)
	{
		if (condition.apply(webDriver) == null)
		{
			return true;
		}
		
		return false;
	}

	public String toString()
	{
		return "check that " + condition.toString() + " is not null";
	}
}
