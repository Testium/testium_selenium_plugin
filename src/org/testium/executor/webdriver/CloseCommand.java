/**
 * 
 */
package org.testium.executor.webdriver;

import java.io.File;

import org.testium.executor.TestStepCommandExecutor;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterNotFoundException;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan
 *
 */
public class CloseCommand implements TestStepCommandExecutor
{
	private static String COMMAND = "close";

	private WebInterface myInterface;
	
    /**
	 * 
	 */
	public CloseCommand( WebInterface aWebInterface )
	{
		myInterface = aWebInterface;
	}

	@Override
	public String getCommand()
	{
		return COMMAND;
	}


	@Override
	public TestStepResult execute( TestStepSimple aStep,
	                               RunTimeData aVariables,
	                               File aLogDir ) throws ParameterNotFoundException
	{
		ParameterArrayList parameters = aStep.getParameters();
		verifyParameters(parameters);

		TestStepResult result = new TestStepResult( aStep );
		myInterface.getDriver().close();

		return result;
	}

	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws ParameterNotFoundException
	{
		return true;
	}

}
