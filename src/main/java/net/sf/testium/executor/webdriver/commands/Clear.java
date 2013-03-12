/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 clear command
 * 
 * @author Arjan Kranenburg
 *
 */
public class Clear extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "clear";
	private static final String PAR_ELEMENT = "element";

	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			PAR_ELEMENT, WebElement.class, false, false, true, false );

    /**
	 * 
	 */
	public Clear( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		WebElement element = obtainElement( aVariables, parameters, PARSPEC_ELEMENT );

		element.clear();
	}
}
