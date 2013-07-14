/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.CheckString;
import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.general.CheckString.MATCH;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Command for checking the text of a WebElement
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckText extends GenericSeleniumCommandExecutor {
	private static final String COMMAND = "checkText";

	private static final String PAR_ELEMENT = "element";
	private static final String PAR_EXPECTED = "expected";

	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			PAR_ELEMENT, WebElement.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_EXPECTED = new SpecifiedParameter( 
			PAR_EXPECTED, String.class, false, true, true, true );

	public CheckText( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_EXPECTED );
		this.addParamSpec( CheckString.PARSPEC_MATCH );
		this.addParamSpec( CheckString.PARSPEC_CASE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {


		WebElement element = obtainElement( aVariables, parameters, PARSPEC_ELEMENT );
		String expectedText = (String) obtainValue( aVariables, parameters, PARSPEC_EXPECTED );
		String actualText = getActualText(element);

		String matchStr = (String) this.obtainOptionalValue(aVariables, parameters, CheckString.PARSPEC_MATCH);
		MATCH match = MATCH.enumOf(matchStr);
		boolean caseSensitive = (Boolean) this.obtainOptionalValue(aVariables, parameters, CheckString.PARSPEC_CASE);

		String elementName = parameters.get(PAR_ELEMENT).getName();
		result.setDisplayName( this.toString() + " " + elementName + " " + expectedText );

		CheckString.checkString(actualText, expectedText, match, caseSensitive, "Actual Text");
	}

	protected static String getActualText( WebElement elm )
	{
		if ( elm == null )
		{ 
			return "";
		} //else
		
		if ( elm.getTagName().equalsIgnoreCase("input") || 
			 elm.getTagName().equalsIgnoreCase("textarea")	)
		{
			String text = elm.getAttribute("value");
			return text == null ? "" : text;
		} //else
		
		return elm.getText();
	}
}
