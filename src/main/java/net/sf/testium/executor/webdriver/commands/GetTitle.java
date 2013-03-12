/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebDriver;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Executes the Selenium 2.0 getTitle command
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetTitle extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "getTitle";
	private static final String PAR_TITLE = "title";

	private static final SpecifiedParameter PARSPEC_TITLE = new SpecifiedParameter( 
			PAR_TITLE, String.class, false, true, true, false );

    /**
	 * 
	 */
	public GetTitle( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_TITLE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		String variableName = (String) obtainValue(aVariables, parameters, PARSPEC_TITLE);

		WebDriver webDriver = this.getDriver();
		String title = webDriver.getTitle();

		RunTimeVariable rtVariable = new RunTimeVariable( variableName, title );
		aVariables.add(rtVariable);

		result.setDisplayName( this.toString() + " " + variableName + "=\"" + title + "\"" );
	}

}
