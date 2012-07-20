package net.sf.testium.executor.webdriver.commands;

import java.util.List;

import net.sf.testium.executor.general.CheckListSize;
import net.sf.testium.executor.general.SpecifiedParameter;
import net.sf.testium.selenium.SimpleElementList;
import net.sf.testium.systemundertest.SutInterface;

import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterArrayList;
import org.testtoolinterfaces.utils.RunTimeData;
public class CheckListSize_modified extends CheckListSize
{
//	private static final String COMMAND = "checkListSize";
//
	private static final String PAR_LIST = "list";
//	private static final String PAR_SIZE = "size";
//
	private static final SpecifiedParameter PARSPEC_LIST = new SpecifiedParameter( 
			PAR_LIST, List.class, false, false, true, false );
//	private static final SpecifiedParameter PARSPEC_SIZE = new SpecifiedParameter( 
//			PAR_SIZE, Integer.class, false, true, false, false );
//
	/**
	 *
	 */
	public CheckListSize_modified( SutInterface aSutInterface )
	{
		super( aSutInterface );
	}

	@Override
	protected void doExecute(RunTimeData aVariables,
			ParameterArrayList parameters, TestStepResult result)
			throws Exception 
	{
System.out.println("Running modified checkListSize");
		if (aVariables.getType(PAR_LIST).isAssignableFrom(SimpleElementList.class) )
		{
			SimpleElementList elList = (SimpleElementList) this.obtainValue(aVariables, parameters, PARSPEC_LIST);
			elList.refresh();
System.out.println("List now has size: " + elList.size() );
		}
		else
		{
			super.doExecute(aVariables, parameters, result);
		}
//		int expectedSize = (Integer) this.obtainOptionalValue(aVariables, parameters, PARSPEC_SIZE);
//
//		String listName = parameters.get(PAR_LIST).getName();
//		result.setDisplayName( result.getDisplayName() + " " + listName + " " + expectedSize );
//
//		if ( list.size() != expectedSize )
//		{
//			throw new TestSuiteException( "List size was " + list.size() + ". Expected " + expectedSize );
//		}
	}
}
