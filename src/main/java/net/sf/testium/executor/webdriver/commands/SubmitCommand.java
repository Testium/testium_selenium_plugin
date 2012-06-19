///**
// * 
// */
//package net.sf.testium.executor.webdriver.commands;
//
//import java.io.File;
//
//import org.openqa.selenium.StaleElementReferenceException;
//import org.openqa.selenium.WebElement;
//import net.sf.testium.executor.webdriver.WebInterface;
//import org.testtoolinterfaces.testresult.TestStepResult;
//import org.testtoolinterfaces.testresult.TestResult.VERDICT;
//import org.testtoolinterfaces.testsuite.Parameter;
//import org.testtoolinterfaces.testsuite.ParameterArrayList;
//import org.testtoolinterfaces.testsuite.ParameterVariable;
//import org.testtoolinterfaces.testsuite.TestStep;
//import org.testtoolinterfaces.testsuite.TestSuiteException;
//import org.testtoolinterfaces.utils.RunTimeData;
//
///**
// * Executes the Selenium 2.0 submit command
// * 
// * @author Arjan Kranenburg
// *
// */
//public class SubmitCommand extends WebDriverCommandExecutor
//{
//	private static final String COMMAND = "submit";
//	private static final String PAR_ELEMENT = "element";
//
//    /**
//	 * 
//	 */
//	public SubmitCommand( WebInterface aWebInterface )
//	{
//		super( COMMAND, aWebInterface );
//	}
//
//	@Override
//	public TestStepResult execute( TestStep aStep,
//	                               RunTimeData aVariables,
//	                               File aLogDir ) throws TestSuiteException
//	{
//		ParameterArrayList parameters = aStep.getParameters();
//		verifyParameters(parameters);
//
//		TestStepResult result = new TestStepResult( aStep );
//
//		ParameterVariable variablePar = (ParameterVariable) parameters.get(PAR_ELEMENT);
//		WebElement element = getVariableValueAs(WebElement.class, variablePar, aVariables);
//
//		try
//		{
//			element.submit();
//			result.setResult( VERDICT.PASSED );
//		}
//		catch( StaleElementReferenceException sere )
//		{
//			result.setResult( VERDICT.FAILED );
//			result.addComment( sere.getLocalizedMessage() );
//		}
//
//		return result;
//	}
//
//	@Override
//	public boolean verifyParameters( ParameterArrayList aParameters ) throws TestSuiteException
//	{
//		// Check the Element Parameter Variable which is returned with the found web-element
//		Parameter elementPar = aParameters.get(PAR_ELEMENT);
//		if ( elementPar == null )
//		{
//			throw new TestSuiteException( "Parameter " + PAR_ELEMENT + " is not set",
//			                              getInterfaceName() + "." + COMMAND );
//		}
//		
//		verifyParameterVariable(elementPar);
//
//		return true;
//	}
//
//}
