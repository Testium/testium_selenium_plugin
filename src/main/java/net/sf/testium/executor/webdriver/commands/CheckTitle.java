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
 * Executes the Selenium 2.0 getTitle command and validates the result against a parameter
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckTitle  extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "checkTitle";
	private static final String PAR_TITLE = "title";

	private static final SpecifiedParameter PARSPEC_TITLE = new SpecifiedParameter( 
			PAR_TITLE, String.class, false, true, true, false );

    /**
	 * 
	 */
	public CheckTitle( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_TITLE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		String expectedTitle = (String) obtainValue( aVariables, parameters, PARSPEC_TITLE );

		String title = this.getDriver().getTitle();
		if ( ! title.equals( expectedTitle ) ) {
			throw new Exception( PAR_TITLE + " has value '" + title
	                   + "'. Expected '" + expectedTitle + "'" );
		}
	}
}
