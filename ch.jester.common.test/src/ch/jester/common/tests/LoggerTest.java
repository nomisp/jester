package ch.jester.common.tests;

import junit.framework.Assert;

import org.junit.Test;

import ch.jester.common.logging.IExtendedStatus;
import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.common.tests.testservice.LoggerFactoryMock;
import ch.jester.common.tests.testservice.LoggerMock;
import ch.jester.commonservices.api.logging.ILogger;


public class LoggerTest extends ActivatorProviderForTestCase{
	/**
	 * Testet ob ein Logging Service vorhanden ist
	 * Test-ID: U-CO-7
	 */
	@Test
	public void testGetLoggingService(){
		ILogger logger = getActivator().getActivationContext().getLogger();
		Assert.assertNotNull("ILogger is Null", logger);
	}
	/**
	 * Testet mit einem Mock, ob debug() und info() funktionieren. (Debug on)
	 * Test-ID: U-CO-8
	 */
	@Test
	public void testInfoAndDebug(){
		LoggerFactoryMock loggerFactory = new LoggerFactoryMock();
		loggerFactory.setDebug(true);
		ILogger logger = loggerFactory.getLogger(this.getClass());
		LoggerMock mock = loggerFactory.getLogger();
		mock.expected(IExtendedStatus.INFO,"Hello World", null);
		mock.expected(IExtendedStatus.DEBUG,"Hello World - Debug", null);
		mock.record();
		
		logger.info("Hello World");
		logger.debug("Hello World - Debug");
		
		mock.validate();
	}
	/**
	 * Testet mit einem Mock, ob debug() und info() funktionieren. (Debug off)
	 * Test-ID: U-CO-9
	 */
	@Test
	public void testInfoAndDebug2(){
		LoggerFactoryMock loggerFactory = new LoggerFactoryMock();
		loggerFactory.setDebug(false);
		ILogger logger = loggerFactory.getLogger(this.getClass());
		LoggerMock mock = loggerFactory.getLogger();
		mock.expected(IExtendedStatus.INFO,"Hello World", null);
		//mock.expected(IExtendedStatus.DEBUG,"Hello World - Debug", null);
		mock.record();
		
		logger.info("Hello World");
		logger.debug("Hello World - Debug");
		
		mock.validate();
	}
	
	/**
	 * Test-ID: U-CO-10
	 */
	@Test
	public void testLogService(){
		getActivator().getActivationContext().getLogger().info("Hallo Welt");
	}
	
}
