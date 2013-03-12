/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Centralizes (vertically) a WebElement on the page
 * 
 * @author Arjan Kranenburg
 *
 */
public class CentralizeItem extends GenericSeleniumCommandExecutor {
	private static final String COMMAND = "centralizeItem";

	private static final String PAR_ELEMENT = "element";

	public static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			PAR_ELEMENT, WebElement.class, false, false, true, false );
	
	public CentralizeItem( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		WebElement element = (WebElement) this.obtainElement(aVariables, parameters, PARSPEC_ELEMENT);

		String elementName = parameters.get(PAR_ELEMENT).getName();
		result.setDisplayName( this.toString() + " " + elementName );

		this.centralizeElement(element);
	}
	
    private int getLocationY( WebElement anElement ) {
   		return anElement.getLocation().getY();
    }

    private int getWindowSize( RemoteWebDriver aDriver ) {
    	return aDriver.manage().window().getSize().height;
    }

	/**
	 * @param anElement
	 */
	protected void centralizeElement(WebElement anElement) {
		if (this.getDriver() instanceof RemoteWebDriver) {
            RemoteWebDriver driver = (RemoteWebDriver) this.getDriver();
            int scrollToY = getLocationY(anElement) - getWindowSize( driver )/2;
            driver.executeScript("scrollTo(0," + scrollToY + ");");
        }
	}
}
