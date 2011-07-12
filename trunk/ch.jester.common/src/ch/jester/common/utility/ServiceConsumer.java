package ch.jester.common.utility;

import ch.jester.common.activator.internal.CommonActivator;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.util.ServiceUtility;

/**
 * Klasse welche <br> - ServiceUtility mService<br> - ILogger mLogger<br> initialisiert zur Verfügung stellt.
 */
public class ServiceConsumer {
	/**
	 * @uml.property  name="mService"
	 * @uml.associationEnd  
	 */
	private ServiceUtility mService = ServiceUtility.getUtility();
	/**
	 * @uml.property  name="mLogger"
	 * @uml.associationEnd  
	 */
	private ILogger mLogger = CommonActivator.getInstance().getActivationContext().getLogger(ServiceConsumer.this.getClass());
	public ServiceUtility getServiceUtility(){
		return mService;
	}
	public ILogger getLogger(){
		return mLogger;
	}
}
