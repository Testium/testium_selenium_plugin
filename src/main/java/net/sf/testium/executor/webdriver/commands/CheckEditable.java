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
 * Checks if an element is editable or not editable
 *
 * @author Arjan Kranenburg
 *
 */
public class CheckEditable extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "checkEditable";

	private static final String PAR_ELEMENT = "element";
	private static final String PAR_EDITABLE = "editable";

	private static final String ELM_INPUT = "input";
	private static final String ELM_TEXTAREA = "textarea";

	private static final String ATTR_DISABLED = "disabled";
	private static final String ATTR_HIDDEN = "hidden";
	private static final String ATTR_READONLY = "readonly";
	private static final String ATTR_ISCONTENTEDITABLE = "iscontenteditable";
	

	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
		PAR_ELEMENT, WebElement.class, false, false, true, false );

	private static final SpecifiedParameter PARSPEC_EDITABLE = new SpecifiedParameter( 
		PAR_EDITABLE, Boolean.class, true, true, false, false )
		.setDefaultValue( true );

	/**
	 *
	 */
	public CheckEditable( WebInterface anInterface )
	{
		super( COMMAND, anInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_EDITABLE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		WebElement element = obtainElement( aVariables, parameters, PARSPEC_ELEMENT );
		Boolean editable = (Boolean) this.obtainOptionalValue(aVariables, parameters, PARSPEC_EDITABLE);

		if ( element == null )
		{
			throw new Exception( "Element is not found " + element );
		}
		
		try
		{
			isEditable(element);
			if ( ! editable )
			{
				throw new Exception( "Element is editable" );
			}
		}
		catch ( Exception e )
		{
			if ( editable )
			{
				throw e;
			} // else Exception is what is expected
		}
	}

	/**
	 * @param element
	 * @throws Exception
	 */
	private void isEditable(WebElement element) throws Exception {
		String tagName = element.getTagName();
		if ( tagName.equalsIgnoreCase( ELM_INPUT )
			 || tagName.equalsIgnoreCase( ELM_TEXTAREA ) )
		{
			isEditableForAttribute(element, ATTR_DISABLED);
			isEditableForAttribute(element, ATTR_HIDDEN);
			isEditableForAttribute(element, ATTR_READONLY);
		}
		else if ( element.getAttribute( ATTR_ISCONTENTEDITABLE ) == null )
		{
			throw new Exception( "Content of " + tagName + " is not editable" );
		}
	}

	/**
	 * @param element
	 * @param attribute
	 * @throws Exception
	 */
	private void isEditableForAttribute(WebElement element, String attribute)
			throws Exception
	{
		String tagName = element.getTagName();
		if ( element.getAttribute( attribute ) != null )
		{
			throw new Exception( tagName + " is editable (" + attribute + ")" );
		}
	}
}
