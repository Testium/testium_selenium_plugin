/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

/**
 * Command for checking the text of a WebElement
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetText extends GenericSeleniumCommandExecutor {
	private static final String COMMAND = "getText";

	private static final String PAR_ELEMENT = "element";
	private static final String PAR_VARIABLE = "variable";

	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			PAR_ELEMENT, WebElement.class, false, false, true, false );

	public static final SpecifiedParameter PARSPEC_VARIABLE = new SpecifiedParameter( 
			PAR_VARIABLE, String.class, false, true, false, false );


	public GetText( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_VARIABLE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		WebElement element = obtainElement( aVariables, parameters, PARSPEC_ELEMENT );
		String varName = (String) obtainValue(aVariables, parameters, PARSPEC_VARIABLE);

		String text = CheckText.getActualText(element);

		result.setDisplayName( this.toString() + " " + varName + "=\"" + text + "\"" );

		RunTimeVariable rtVariable = new RunTimeVariable( varName, text );
		aVariables.add(rtVariable);
	}

}
