package org.testium.executor.webdriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.ActionChainsGenerator;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testium.executor.TestStepCommandExecutor;
import org.testium.systemundertest.SutInterface;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterNotFoundException;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan
 *
 *	Facade class to the RemoteWebDriver class of Selenium2.0.
 *  It has to be a facade, because the RemoteWebDriver opens the browser upon creation.
 *  We want it to open the browser only when needed.
 *  It uses the same interfaces as RemoteWebDriver.
 */
public class WebInterface implements SutInterface, WebDriver, JavascriptExecutor,
									 FindsById, FindsByClassName, FindsByLinkText, FindsByName,
									 FindsByCssSelector, FindsByTagName, FindsByXPath,
									 HasInputDevices, HasCapabilities // TODO ,TakesScreenshot
{
	private RemoteWebDriver myDriver;
	private String myDriverName;

	private Hashtable<String, TestStepCommandExecutor> myCommandExecutors;

	/**
	 * 
	 */
	public WebInterface( String aName )
	{
		myDriverName = aName;

		myCommandExecutors = new Hashtable<String, TestStepCommandExecutor>();

		add( new GetCommand( this ) );
		add( new CloseCommand( this ) );
	}

	public RemoteWebDriver getDriver()
	{
		if ( myDriver == null )
		{
			createDriver();
		}
		
		return myDriver;
	}

	@Override
	public ArrayList<TestStepCommandExecutor> getCommandExecutors()
	{
		Trace.println( Trace.GETTER );
		Collection<TestStepCommandExecutor> executorCollection = myCommandExecutors.values();
		return new ArrayList<TestStepCommandExecutor>( executorCollection );
	}

	@Override
	public String getInterfaceName()
	{
		return myDriverName;
	}

	
	
	
	public void get( String anUrl )
	{
		getDriver().get(anUrl);		
	}

	@Override
	public void close()
	{
		getDriver().close();
	}

	@Override
	public WebElement findElement(By aBy)
	{
		return getDriver().findElement(aBy);
	}

	@Override
	public List<WebElement> findElements(By aBy)
	{
		return getDriver().findElements(aBy);
	}

	@Override
	public String getCurrentUrl()
	{
		return getDriver().getCurrentUrl();
	}

	@Override
	public String getPageSource()
	{
		return getDriver().getPageSource();
	}

	@Override
	public String getTitle()
	{
		return getDriver().getTitle();
	}

	@Override
	public String getWindowHandle()
	{
		return getDriver().getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles()
	{
		return getDriver().getWindowHandles();
	}

	@Override
	public Options manage()
	{
		return getDriver().manage();
	}

	@Override
	public Navigation navigate()
	{
		return getDriver().navigate();
	}

	@Override
	public void quit()
	{
		getDriver().quit();
	}

	@Override
	public TargetLocator switchTo()
	{
		return getDriver().switchTo();
	}

	@Override
	public Object executeAsyncScript(String aScript, Object... anArgs)
	{
		return getDriver().executeAsyncScript(aScript, anArgs);
	}

	@Override
	public Object executeScript(String aScript, Object... anArgs)
	{
		return getDriver().executeScript(aScript, anArgs);
	}

	@Override
	public boolean isJavascriptEnabled()
	{
		return getDriver().isJavascriptEnabled();
	}

	@Override
	public WebElement findElementById(String aUsing)
	{
		return getDriver().findElementById(aUsing);
	}

	@Override
	public List<WebElement> findElementsById(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public WebElement findElementByClassName(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<WebElement> findElementsByClassName(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public WebElement findElementByLinkText(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public WebElement findElementByPartialLinkText(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<WebElement> findElementsByLinkText(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<WebElement> findElementsByPartialLinkText(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public WebElement findElementByName(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<WebElement> findElementsByName(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public WebElement findElementByCssSelector(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<WebElement> findElementsByCssSelector(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public WebElement findElementByTagName(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<WebElement> findElementsByTagName(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public WebElement findElementByXPath(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<WebElement> findElementsByXPath(String using)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ActionChainsGenerator actionsBuilder()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Keyboard getKeyboard()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Mouse getMouse()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Capabilities getCapabilities()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getCommands()
	{
		Trace.println( Trace.GETTER );
		return Collections.list(myCommandExecutors.keys());
	}

	@Override
	public boolean hasCommand(String aCommand)
	{
		Trace.println( Trace.UTIL );
		ArrayList<String> commands = getCommands();
		return commands.contains(aCommand);
	}

	public boolean verifyParameters( String aCommand,
	                                 ParameterArrayList aParameters )
		   throws ParameterNotFoundException
	{
		TestStepCommandExecutor executor = this.getCommandExecutor(aCommand);
		return executor.verifyParameters(aParameters);
	}

	@Override
	public TestStepCommandExecutor getCommandExecutor(String aCommand)
	{
		Trace.println( Trace.GETTER );
		return myCommandExecutors.get(aCommand);
	}

	private void createDriver()
	{
		Trace.println( Trace.UTIL );
		myDriver = new FirefoxDriver();
	}

	private void add( TestStepCommandExecutor aCommandExecutor )
	{
		Trace.println( Trace.UTIL );
		String command = aCommandExecutor.getCommand();
		myCommandExecutors.put(command, aCommandExecutor);
	}
}
