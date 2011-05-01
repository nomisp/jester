package ch.jester.common.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.jester.system.calculator.test.CalculatorTest;
import ch.jester.system.calculator.test.FideCalculatorTest;
import ch.jester.system.pairing.test.PairingSystemTest;
import ch.jester.system.ranking.test.RankingSystemTest;



@RunWith(Suite.class)
@SuiteClasses( {ImporterTest.class, ActivatorTest.class, CallerUtilityTest.class,  ServiceUtilityTest.class, LoggerTest.class,
	CalculatorTest.class, FideCalculatorTest.class, PairingSystemTest.class, RankingSystemTest.class})
public class AllTests {

}
