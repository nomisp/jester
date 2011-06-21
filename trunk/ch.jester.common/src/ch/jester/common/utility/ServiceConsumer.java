package ch.jester.common.utility;

import ch.jester.common.activator.internal.CommonActivator;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Klasse welche <br>
 * - ServiceUtility mService<br>
 * - ILogger mLogger<br>
 * initialisiert zur Verfügung stellt.
 *
 */
public class ServiceConsumer {
	private ServiceUtility mService = ServiceUtility.getUtility();
	private ILogger mLogger = CommonActivator.getInstance().getActivationContext().getLogger();
	public ServiceUtility getServiceUtility(){
		return mService;
	}
	public ILogger getLogger(){
		return mLogger;
	}
}
