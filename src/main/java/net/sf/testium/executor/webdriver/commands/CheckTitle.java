/**
 * 
 */
package net.sf.testium.executor.webdriver.commands;

import java.util.ArrayList;

import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.executor.webdriver.WebInterface;

import org.testtoolinterfaces.testresult.TestStepCommandResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * Executes the Selenium 2.0 getTitle command and validates the result against a parameter
 * 
 * @author Arjan Kranenburg
 *
 */
public class CheckTitle  extends GenericSeleniumCommandExecutor
{
	private static final String COMMAND = "checkTitle";
	private static final String PAR_TITLE = "title";

	private static final SpecifiedParameter PARSPEC_TITLE = new SpecifiedParameter( 
			PAR_TITLE, String.class, false, true, true, false );
	private static final SpecifiedParameter PARSPEC_MATCH = new SpecifiedParameter( 
			"match", String.class, true, true, true, false )
			.setDefaultValue("exact");

	private static final SpecifiedParameter PARSPEC_CASE = new SpecifiedParameter( 
			"case", Boolean.class, true, true, true, false )
			.setDefaultValue( true );

    /**
	 * 
	 */
	public CheckTitle( WebInterface aWebInterface )
	{
		super( COMMAND, aWebInterface, new ArrayList<SpecifiedParameter>() );

		this.addParamSpec( PARSPEC_TITLE );
		this.addParamSpec( PARSPEC_MATCH );
		this.addParamSpec( PARSPEC_CASE );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepCommandResult result)
			throws Exception {
		String expectedTitle = (String) obtainValue( aVariables, parameters, PARSPEC_TITLE );
		String match = (String) this.obtainOptionalValue(aVariables, parameters, PARSPEC_MATCH);
		boolean caseSensitive = (Boolean) this.obtainOptionalValue(aVariables, parameters, PARSPEC_CASE);


		String title = this.getDriver().getTitle();
		
		if ( match.equalsIgnoreCase( "exact" ) )
		{
			checkExact(expectedTitle, title, caseSensitive,
					"Actual Text: \"" + title + "\" is not equal to: \"" + expectedTitle + "\"");
			return;
		}
		else if ( match.equalsIgnoreCase( "contains" ) )
		{
			checkContains(expectedTitle, title, caseSensitive,
					"Actual Text: \"" + title + "\" does not contain: \"" + expectedTitle + "\"");
			return;
		}
		else if ( match.equalsIgnoreCase( "startsWith" ) )
		{
			checkStartsWith(expectedTitle, title, caseSensitive,
					"Actual Text: \"" + title + "\" does not start with: \"" + expectedTitle + "\"" );
			return;
		}
		else if ( match.equalsIgnoreCase( "endsWith" ) )
		{
			checkEndsWith(expectedTitle, title, caseSensitive,
					"Actual Text: \"" + title + "\" does not end with: \"" + expectedTitle + "\"" );
			return;
		}
		else
		{
			throw new Exception( "match criteria \"" + match + "\" is not supported. Only exact, contains, startsWith, or endsWith" );
		}
	}

	/**
	 * @param expectedText
	 * @param actualText
	 * @param caseSensitive
	 * @param message
	 * @throws Exception
	 */
	private void checkExact(String expectedText, String actualText,
			boolean caseSensitive, String message) throws Exception {
		if ( caseSensitive ) {
			if ( ! actualText.equals(expectedText) ) {
				throw new Exception( message );
			}
		} else {
			if ( ! actualText.equalsIgnoreCase(expectedText) ) {
				throw new Exception( message + " (ignoring case)" );
			}
		}
	}
	
	/**
	 * @param expectedText
	 * @param actualText
	 * @param caseSensitive
	 * @param message
	 * @throws Exception
	 */
	private void checkContains(String expectedText, String actualText,
			boolean caseSensitive, String message) throws Exception {
		if ( caseSensitive ) {
			if ( ! actualText.contains(expectedText) ) {
				throw new Exception( message );
			}
		} else {
			String expectedText_lowerCase = expectedText.toLowerCase();
			String actualText_lowerCase = actualText.toLowerCase();
			if ( ! actualText_lowerCase.contains(expectedText_lowerCase) ) {
				throw new Exception( message + " (ignoring case)" );
			}
		}
	}

	/**
	 * @param expectedText
	 * @param actualText
	 * @param caseSensitive
	 * @param message
	 * @throws Exception
	 */
	private void checkStartsWith(String expectedText, String actualText,
			boolean caseSensitive, String message) throws Exception {
		if ( caseSensitive ) {
			if ( ! actualText.startsWith(expectedText) ) {
				throw new Exception( message );
			}
		} else {
			String expectedText_lowerCase = expectedText.toLowerCase();
			String actualText_lowerCase = actualText.toLowerCase();
			if ( ! actualText_lowerCase.startsWith(expectedText_lowerCase) ) {
				throw new Exception( message + " (ignoring case)" );
			}
		}
	}

	/**
	 * @param expectedText
	 * @param actualText
	 * @param caseSensitive
	 * @param message
	 * @throws Exception
	 */
	private void checkEndsWith(String expectedText, String actualText,
			boolean caseSensitive, String message) throws Exception {
		if ( caseSensitive ) {
			if ( ! actualText.endsWith(expectedText) ) {
				throw new Exception( message );
			}
		} else {
			String expectedText_lowerCase = expectedText.toLowerCase();
			String actualText_lowerCase = actualText.toLowerCase();
			if ( ! actualText_lowerCase.endsWith(expectedText_lowerCase) ) {
				throw new Exception( message + " (ignoring case)" );
			}
		}
	}
}
