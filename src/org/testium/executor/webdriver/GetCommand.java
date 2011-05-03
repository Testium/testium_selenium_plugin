/**
 * 
 */
package org.testium.executor.webdriver;

import java.io.File;

import org.testium.executor.TestStepCommandExecutor;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.Parameter;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.testsuite.ParameterNotFoundException;
import org.testtoolinterfaces.testsuite.TestStepSimple;
import org.testtoolinterfaces.utils.RunTimeData;

/**
 * @author Arjan
 *
 */
public class GetCommand implements TestStepCommandExecutor
{
	public static String PAR_URL = "URL";

	private static String COMMAND = "get";

	private WebInterface myInterface;
	
    /**
	 * 
	 */
	public GetCommand( WebInterface aWebInterface )
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
		Parameter urlPar = parameters.get(PAR_URL);
		String url = urlPar.getValueAsString();

		myInterface.getDriver().get(url);

		return result;
	}


	@Override
	public boolean verifyParameters( ParameterArrayList aParameters ) throws ParameterNotFoundException
	{
		Parameter urlPar = aParameters.get(PAR_URL);
		if ( urlPar == null )
		{
			throw new ParameterNotFoundException( myInterface.getInterfaceName(),
			                                      COMMAND,
			                                      PAR_URL );
		}
		
		if ( urlPar.getValueAsString().isEmpty() )
		{
			// TODO Use/create a better exception here
			throw new ParameterNotFoundException( myInterface.getInterfaceName(),
			                                      COMMAND,
			                                      PAR_URL );
		}
		
		return true;
	}

}
