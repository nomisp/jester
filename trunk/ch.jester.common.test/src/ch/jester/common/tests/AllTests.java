package ch.jester.common.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.jester.model.settings.tests.SettingsTest;
import ch.jester.system.calculator.test.CalculatorTest;
import ch.jester.system.calculator.test.FideCalculatorTest;
import ch.jester.system.pairing.test.PairingSystemTest;
import ch.jester.system.pairing.test.RoundRobinTest;
import ch.jester.system.ranking.test.RankingSystemTest;



@RunWith(Suite.class)
@SuiteClasses( { ImportExportTest.class, DaoEqualityTest.class,ImporterTest.class, ActivatorTest.class, CallerUtilityTest.class,  ServiceUtilityTest.class, LoggerTest.class,
	CalculatorTest.class, FideCalculatorTest.class, PairingSystemTest.class, RoundRobinTest.class, RankingSystemTest.class, 
	SettingsTest.class})
public class AllTests {

}
