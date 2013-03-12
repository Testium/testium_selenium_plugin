package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Checks if the element is selected (or not)
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckSelected extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "checkSelected";

	public static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			"element", WebElement.class, false, false, true, false );
	public static final SpecifiedParameter PARSPEC_EXPECTED = new SpecifiedParameter( 
			"expected", Boolean.class, true, true, false, true )
			.setDefaultValue( true );

	public CheckSelected( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_EXPECTED );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		WebElement element = obtainElement(aVariables, parameters, PARSPEC_ELEMENT);
		boolean expected = obtainOptionalValue(aVariables, parameters, PARSPEC_EXPECTED);

		if ( expected ) {
			if ( ! element.isSelected() ) {
				throw new Exception( "Element is unexpectedly not selected." );
			}
		} else {
			if ( element.isSelected() ) {
				throw new Exception( "Element is unexpectedly selected" );
			}
		}
	}
}
