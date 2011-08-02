package ch.jester.common.tests;

import junit.framework.Assert;

import org.junit.Test;

import ch.jester.common.utility.CallerUtility;
import ch.jester.common.utility.CallerUtility.Caller;

public class CallerUtilityTest{

	/**
	 * Test-ID: U-CO-4
	 */
	@Test
	public void testGetCaller(){
		Caller caller = CallerUtility.getCaller(1);
		Assert.assertEquals("Class wrong", caller.getCallerClass(),this.getClass().getName());
		Assert.assertEquals("Method wrong", caller.getCallerMethod(),"testGetCaller");
		
		caller = CallerUtility.getCaller();
		Assert.assertEquals("Class wrong", caller.getCallerClass(),this.getClass().getName());
		Assert.assertEquals("Method wrong", caller.getCallerMethod(),"testGetCaller");
	}
}
