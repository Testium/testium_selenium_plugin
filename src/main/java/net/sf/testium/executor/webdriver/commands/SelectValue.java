/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Centralizes (vertically) a WebElement on the page
 * 
 * @author Arjan Kranenburg
 *
 */
public class SelectValue extends GenericSeleniumCommandExecutor {
	private static final String COMMAND = "select";

	private static final String PAR_ELEMENT = "element";
	private static final String PAR_VALUE = "value";
	private static final String PAR_VISIBLE_TEXT = "visibleText";
	private static final String PAR_INDEX = "index";

	public static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			PAR_ELEMENT, WebElement.class, false, false, true, false );

	public static final SpecifiedParameter PARSPEC_VALUE = new SpecifiedParameter( 
			PAR_VALUE, String.class, true, true, false, false )
		.setDefaultValue("");

	public static final SpecifiedParameter PARSPEC_VISIBLE_TEXT = new SpecifiedParameter( 
			PAR_VISIBLE_TEXT, String.class, true, true, false, false )
		.setDefaultValue("");

	public static final SpecifiedParameter PARSPEC_INDEX = new SpecifiedParameter( 
			PAR_INDEX, Integer.class, true, true, false, false )
		.setDefaultValue(0);

	public SelectValue( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_VALUE );
		this.addParamSpec( PARSPEC_VISIBLE_TEXT );
		this.addParamSpec( PARSPEC_INDEX );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception
	{

		WebElement element = (WebElement) this.obtainElement(aVariables, parameters, PARSPEC_ELEMENT);

		String value = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_VALUE);
		String visibleText = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_VISIBLE_TEXT);
		Integer index = (Integer) this.obtainOptionalValue(aVariables, parameters, PARSPEC_INDEX);

		String elementName = parameters.get(PAR_ELEMENT).getName();
		result.setDisplayName( result.getDisplayName() + " " + elementName );

		Select select = new Select( element );
		if ( ! value.isEmpty() )
		{
			select.selectByValue(value);
		}
		else if ( ! visibleText.isEmpty() )
		{
			select.selectByVisibleText(visibleText);
		}
		else
		{
			select.selectByIndex(index);
		}
	}
}
