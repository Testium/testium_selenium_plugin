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
import org.testtoolinterfaces.testsuite.ParameterVariable;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Executes the VIND login command
 * 
 * @author Arjan Kranenburg
 *
 */
public class GetAttribute extends GenericSeleniumCommandExecutor {
	private static final String COMMAND = "getAttribute";

	private static final String PAR_ELEMENT = "element";
	private static final String PAR_ATTRIBUTE = "attribute";
	private static final String PAR_VARIABLE = "variable";

	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			PAR_ELEMENT, WebElement.class, false, false, true, false );

	public static final SpecifiedParameter PARSPEC_ATTRIBUTE = new SpecifiedParameter( 
			PAR_ATTRIBUTE, String.class, false, true, false, false );

	public static final SpecifiedParameter PARSPEC_VARIABLE = new SpecifiedParameter( 
			PAR_VARIABLE, String.class, false, true, false, false );


	public GetAttribute( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_ATTRIBUTE );
		this.addParamSpec( PARSPEC_VARIABLE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception
	{
		WebElement element = obtainElement( aVariables, parameters, PARSPEC_ELEMENT );
		String attribute = (String) obtainValue(aVariables, parameters, PARSPEC_ATTRIBUTE);
		String varName = (String) obtainValue(aVariables, parameters, PARSPEC_VARIABLE);

		ParameterVariable parVar = (ParameterVariable) parameters.get(PAR_ELEMENT);
		String elementName = parVar.getVariableName();

		result.setDisplayName( this.toString() + " \"" + elementName + "\" \"" + attribute + "\" -> \"" + varName + "\"" );

		String attributeValue = element.getAttribute(attribute);
		RunTimeVariable rtVariable = new RunTimeVariable( varName, attributeValue );
		aVariables.add(rtVariable);
	}
}
