/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.utils.RunTimeData;

import net.sf.testium.executor.webdriver.WebInterface;
import net.sf.testium.executor.general.SpecifiedParameter;

/**
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckAttribute extends GenericSeleniumCommandExecutor {
	private static final String COMMAND = "checkAttribute";

	private static final String PAR_ELEMENT = "element";
	private static final String PAR_ATTRIBUTE = "attribute";
	private static final String PAR_EXPECTED = "expected";
	private static final String PAR_CASE = "case";

	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			PAR_ELEMENT, WebElement.class, false, false, true, false );

	public static final SpecifiedParameter PARSPEC_ATTRIBUTE = new SpecifiedParameter( 
			PAR_ATTRIBUTE, String.class, false, true, false, false );

	private static final SpecifiedParameter PARSPEC_EXPECTED = new SpecifiedParameter( 
			PAR_EXPECTED, String.class, false, true, true, true );

	private static final SpecifiedParameter PARSPEC_CASE = new SpecifiedParameter( 
			PAR_CASE, Boolean.class, true, true, false, false )
		.setDefaultValue( true );

	public CheckAttribute( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_ATTRIBUTE );
		this.addParamSpec( PARSPEC_EXPECTED );
		this.addParamSpec( PARSPEC_CASE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		WebElement element = obtainElement( aVariables, parameters, PARSPEC_ELEMENT );
		String attribute = (String) obtainValue(aVariables, parameters, PARSPEC_ATTRIBUTE);
		String expected = (String) obtainValue(aVariables, parameters, PARSPEC_EXPECTED);
		Boolean caseSensitive = (Boolean) this.obtainOptionalValue(aVariables, parameters, PARSPEC_CASE);

		ParameterVariable parVar = (ParameterVariable) parameters.get(PAR_ELEMENT);
		String elementName = parVar.getVariableName();

		result.setDisplayName( result.getDisplayName() + " \"" + elementName + "\" \"" + attribute + "\" = \"" + expected + "\"" );

		String actualValue = element.getAttribute(attribute);
		String errorMsg = "Actual Value: \"" + actualValue + "\" is not equal to: \"" + expected + "\"";

		if ( caseSensitive ) {
			if ( ! actualValue.equals(expected) ) {
				throw new Exception( errorMsg );
			}
		} else {
			if ( ! actualValue.equalsIgnoreCase(expected) ) {
				throw new Exception( errorMsg + " (ignoring case)" );
			}
		}
	}
}
