/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;
import net.sf.testium.selenium.SimpleElementList;
import net.sf.testium.selenium.SmartWebElementList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.RunTimeVariable;

/**
 * Command for defining a WebElement without actually finding it.
 * 
 * @author Arjan Kranenburg
 *
 */
public class DefineElementList extends GenericSeleniumCommandExecutor {
	private static final SpecifiedParameter PARSPEC_BY = new SpecifiedParameter( 
			"by", By.class, false, true, false, false );

	private static final SpecifiedParameter PARSPEC_NAME = new SpecifiedParameter( 
			"name", String.class, false, true, false, false );

	public static final SpecifiedParameter PARSPEC_BASEELEMENT = new SpecifiedParameter( 
			"baseElement", WebElement.class, true, false, true, false );

	private static final String COMMAND = "defineElementList";

	public DefineElementList( WebInterface aWebInterface ) {
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_BY );
		this.addParamSpec( PARSPEC_NAME );
		this.addParamSpec( PARSPEC_BASEELEMENT );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception {

		By by = (By) obtainValue( aVariables, parameters, PARSPEC_BY );
		String name = (String) obtainValue( aVariables, parameters, PARSPEC_NAME );

		SmartWebElementList smartElementList;
		WebElement baseElement = this.obtainElement(aVariables, parameters, PARSPEC_BASEELEMENT);
		if ( baseElement != null )
		{
			smartElementList = new SimpleElementList( by, this.getInterface(), null, baseElement );
		}
		else
		{
			smartElementList = new SimpleElementList( by, this.getInterface() );
		}

		RunTimeVariable rtVariable = new RunTimeVariable( name, smartElementList );
		aVariables.add(rtVariable);
	}
}
