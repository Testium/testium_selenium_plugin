/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.openqa.selenium.By;
import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Command for setting a By variable.
 * 
 * @author Arjan Kranenburg
 *
 */
public class SetByVariable extends GenericSeleniumCommandExecutor {
	private static final SpecifiedParameter PARSPEC_NAME = new SpecifiedParameter( 
			"name", String.class, false, true, false, false );

	private static final SpecifiedParameter PARSPEC_BY = new SpecifiedParameter( 
			"by", String.class, false, true, true, false );

	private static final SpecifiedParameter PARSPEC_TYPE = new SpecifiedParameter( 
			"type", String.class, false, true, false, false );

	private static final String COMMAND = "setByVariable";

	public SetByVariable( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_BY );
		this.addParamSpec( PARSPEC_NAME );
		this.addParamSpec( PARSPEC_TYPE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {

		String byString = (String) obtainValue( aVariables, parameters, PARSPEC_BY );
		String name = (String) obtainValue( aVariables, parameters, PARSPEC_NAME );
		String type = (String) obtainValue( aVariables, parameters, PARSPEC_TYPE );

		By by = WebInterface.getBy(type, byString);

		RunTimeVariable rtVariable = new RunTimeVariable( name, by );
		aVariables.add(rtVariable);
	}
}
