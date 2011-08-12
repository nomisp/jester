package ch.jester.common.tests.testservice;

import ch.jester.common.tests.testservice.api.IDummyService;

/**
 * Implementierung eines DummyServices
 */
public class DefaultDummyService implements IDummyService {

	/*
	 * Gibt immer 55 zurück
	 * 
	 * @see ch.jester.common.tests.testservice.api.IDummyService#get()
	 */
	@Override
	public int get() {
		return 55;
	}

}
