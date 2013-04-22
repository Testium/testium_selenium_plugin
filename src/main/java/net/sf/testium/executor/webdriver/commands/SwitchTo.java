package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.TestSuiteException;
import org.testtoolinterfaces.utils.RunTimeData;

public class SwitchTo extends GenericSeleniumCommandExecutor
{

	private static final String COMMAND = "switchTo";

	private static final String PAR_WINDOW = "window";
	private static final String PAR_FRAME = "frame";
	private static final String PAR_ALERT = "alert";

	private static final SpecifiedParameter PARSPEC_WINDOW = new SpecifiedParameter( 
			PAR_WINDOW, String.class, true, true, true, false ).setDefaultValue("");
	private static final SpecifiedParameter PARSPEC_FRAME = new SpecifiedParameter( 
			PAR_FRAME, String.class, true, true, true, false ).setDefaultValue("");
	private static final SpecifiedParameter PARSPEC_ALERT = new SpecifiedParameter( 
			PAR_ALERT, String.class, true, true, true, false ).setDefaultValue("");

	/**
	 *
	 */
	public SwitchTo( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_WINDOW );
		this.addParamSpec( PARSPEC_FRAME );
		this.addParamSpec( PARSPEC_ALERT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		String window = (String) obtainValue( aVariables, parameters, PARSPEC_WINDOW );
		String frame = (String) obtainValue( aVariables, parameters, PARSPEC_FRAME );
		String alert = (String) obtainValue( aVariables, parameters, PARSPEC_ALERT );

		if ( ! window.isEmpty() ) {
			this.getDriver().switchTo().window( window );			

		} else if ( ! frame.isEmpty() ) {
			this.getDriver().switchTo().frame( frame );			
				
		} else if ( ! alert.isEmpty() ) {
			this.getDriver().switchTo().alert();			
					
		} else {
			throw new TestSuiteException( "One of parameters " + PAR_WINDOW +
					", " + PAR_FRAME + ", and " + PAR_ALERT + " must be set.",
	                this.toString() );
		}
	}
}
