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
 * Executes the Selenium 2.0 getCurrentUrl command
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetCurrentUrl  extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "getCurrentUrl";
	private static final String PAR_URL = "url";

	public static final SpecifiedParameter PARSPEC_URL = new SpecifiedParameter( 
			PAR_URL, String.class, false, true, true, false );

    /**
	 * 
	 */
	public GetCurrentUrl( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_URL );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		String variableName = (String) obtainValue( aVariables, parameters, PARSPEC_URL );

		WebDriver webDriver = this.getDriver();
		String currentUrl = webDriver.getCurrentUrl();

		RunTimeVariable rtVariable = new RunTimeVariable( variableName, currentUrl );
		aVariables.add(rtVariable);

		result.setDisplayName( this.toString() + " " + variableName + "=\"" + currentUrl + "\"" );
	}

}
