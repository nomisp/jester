package org.eclipselabs.p2.rcpupdate.utils.plugin;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.exceptions.ProcessingException;

public class Activator extends AbstractUIActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipselabs.p2.rcpupdate.utils"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private static ILogger mLogger;
	public static void log(IStatus status) {
		mLogger.debug(status.getMessage());
	}
	
	public static void log(Exception e) {
		mLogger.error(e);
	}


	public void startDelegate(BundleContext context){
		plugin = this;
		mLogger = plugin.getActivationContext().getLogger();
	
	}
	
	public void addRepo(String p) throws ProcessingException{
		ServiceReference reference = getActivationContext().getBundleContext().getServiceReference(IProvisioningAgent.SERVICE_NAME);
		addRepos((IProvisioningAgent)getActivationContext().getBundleContext().getService(reference), p);
	}

	private void addRepos(IProvisioningAgent agent, String pUrl) throws ProcessingException{
		IMetadataRepositoryManager repoManager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		IArtifactRepositoryManager arteManager = (IArtifactRepositoryManager) agent.getService(IArtifactRepositoryManager.SERVICE_NAME);

			URL url;
			try {
				url = new URL(pUrl);
				repoManager.addRepository(url.toURI());
				arteManager.addRepository(url.toURI());
			} catch (MalformedURLException e) {
				throw new ProcessingException(e);
			} catch (URISyntaxException e) {
				throw new ProcessingException(e);
			}
	}

	public void stopDelegate(BundleContext context)  {
		plugin = null;
	}

	public static Activator getDefault() {
		return plugin;
	}


}
