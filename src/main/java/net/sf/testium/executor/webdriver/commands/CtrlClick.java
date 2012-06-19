package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Simulates pressing the Ctrl-key and a left-mouse-click
 * 
 * @author Arjan Kranenburg
 *
 */
public class CtrlClick extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "ctrlClick";

	public static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			"element", WebElement.class, false, false,	true, false );

	public CtrlClick( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		WebElement element = obtainElement(aVariables, parameters, PARSPEC_ELEMENT);
		
		Actions builder = new Actions(this.getDriver());

		   builder.keyDown(Keys.CONTROL)
		       .click(element)
		       .keyUp(Keys.CONTROL);
	}
}
