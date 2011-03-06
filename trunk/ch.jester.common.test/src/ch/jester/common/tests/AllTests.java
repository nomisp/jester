package ch.jester.common.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;



@RunWith(Suite.class)
@SuiteClasses( { ActivatorTest.class, CallerUtilityTest.class,  ServiceUtilityTest.class, LoggerTest.class })
public class AllTests {

}
