package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

public class SendKeys extends GenericSeleniumCommandExecutor
{

	private static final String COMMAND = "sendKeys";

	private static final String PAR_ELEMENT = "element";
	private static final String PAR_KEYS = "keys";
	private static final String PAR_SPECIAL_KEY = "specialKey";

	private static final SpecifiedParameter PARSPEC_ELEMENT = new SpecifiedParameter( 
			PAR_ELEMENT, WebElement.class, false, false, true, false );
	private static final SpecifiedParameter PARSPEC_KEYS = new SpecifiedParameter( 
			PAR_KEYS, String.class, true, true, true, true );
	private static final SpecifiedParameter PARSPEC_SPECIAL_KEY = new SpecifiedParameter( 
//			PAR_SPECIAL_KEY, Keys.class, true, true, true, false );
			PAR_SPECIAL_KEY, String.class, true, true, true, false );

	/**
	 *
	 */
	public SendKeys( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_ELEMENT );
		this.addParamSpec( PARSPEC_KEYS );
		this.addParamSpec( PARSPEC_SPECIAL_KEY );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		WebElement element = this.obtainElement(aVariables, parameters, PARSPEC_ELEMENT);

		Object keysToSendObj = obtainValue(aVariables, parameters, PARSPEC_KEYS);
		if ( keysToSendObj instanceof String )
		{
			String keysToSend = (String) keysToSendObj;
			element.sendKeys(keysToSend);
			return;
		} //else
			
		Object keysToSendSpecialObj = obtainValue(aVariables, parameters, PARSPEC_SPECIAL_KEY);
		if ( keysToSendSpecialObj instanceof String ) {
			Keys keysToSendSpecial = Keys.valueOf( (String) keysToSendSpecialObj );
			element.sendKeys(keysToSendSpecial);
			return;
		} //else

		throw new TestSuiteException( "One of parameters " + PAR_KEYS +
				" and " + PAR_SPECIAL_KEY + " must be set.",
                this.toString() );
	}
}
