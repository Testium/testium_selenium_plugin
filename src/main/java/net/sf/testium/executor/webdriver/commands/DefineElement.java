/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.configuration.SeleniumInterfaceConfiguration.SAVE_SOURCE;
import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;
import net.sf.testium.selenium.SimplePageElement;
import net.sf.testium.selenium.SmartWebElement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Command for defining a WebElement without actually finding it.
 * 
 * @author Arjan Kranenburg
 *
 */
public class DefineElement extends GenericSeleniumCommandExecutor {
	private static final SpecifiedParameter PARSPEC_BY = new SpecifiedParameter( 
			"by", By.class, false, true, true, false );

	private static final SpecifiedParameter PARSPEC_NAME = new SpecifiedParameter( 
			"name", String.class, false, true, false, false );

	public static final SpecifiedParameter PARSPEC_BASEELEMENT = new SpecifiedParameter( 
			"baseElement", WebElement.class, true, false, true, false );

	private static final String COMMAND = "defineElement";

	public DefineElement( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_BY );
		this.addParamSpec( PARSPEC_NAME );
		this.addParamSpec( PARSPEC_BASEELEMENT );
		
		this.setSavePageSource(SAVE_SOURCE.NEVER);
		this.setSaveScreenshot(SAVE_SOURCE.NEVER);
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		By by = (By) obtainValue( aVariables, parameters, PARSPEC_BY );
		String name = (String) obtainValue( aVariables, parameters, PARSPEC_NAME );

		SmartWebElement smartElement;
		WebElement baseElement = this.obtainElement(aVariables, parameters, PARSPEC_BASEELEMENT);
		if ( baseElement != null )
		{
			smartElement = new SimplePageElement( by, this.getInterface(), null, baseElement );
		}
		else
		{
			smartElement = new SimplePageElement( by, this.getInterface() );
		}
//		SmartWebElement smartElement = new SimplePageElement( by, this.getInterface() );

		RunTimeVariable rtVariable = new RunTimeVariable( name, smartElement );
		aVariables.add(rtVariable);
	}
}
