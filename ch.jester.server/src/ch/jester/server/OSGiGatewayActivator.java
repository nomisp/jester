package ch.jester.server;

import java.awt.Desktop;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import ch.jester.common.activator.AbstractActivator;
import ch.jester.common.ui.labelprovider.ImageStatusLineContributionItem;
import ch.jester.common.ui.labelprovider.LinkStatusLineContributionItem;
import ch.jester.common.ui.services.IExtendedStatusLineManager;
import ch.jester.common.ui.utility.UIUtility;

/**
 * The activator class controls the plug-in life cycle
 */
public class OSGiGatewayActivator extends AbstractActivator {
	private static Dictionary<String, Object> settings;
	// The plug-in ID
	public static final String PLUGIN_ID = "ch.jester.server"; //$NON-NLS-1$

	// The shared instance
	private static OSGiGatewayActivator plugin;

	public OSGiGatewayActivator() {
	}


	public void startDelegate(BundleContext context) {
		plugin = this;
		Bundle bundle = Platform.getBundle("org.eclipse.equinox.http.registry");
		if (bundle.getState() == Bundle.RESOLVED) {
			try {
				bundle.start(Bundle.START_TRANSIENT);
			} catch (BundleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 

		

		try {
			JettyConfigurator.startServer(PLUGIN_ID + ".jetty",getConfig());
		}
		catch (Exception e) {
		e.printStackTrace();
		}
	}

	public static Dictionary<String, Object> getConfig(){
		if(settings==null){
			settings = new Hashtable<String, Object>();
			settings.put("http.enabled",Boolean.TRUE);
			settings.put("http.port",8080);
			settings.put("http.host","0.0.0.0");
			settings.put("https.enabled",Boolean.FALSE);
			settings.put("context.path","/");
			settings.put("context.sessioninactiveinterval",1800);
			Job job = new Job("updater"){

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					if(!UIUtility.isUIReady()){
						schedule(6000);
						return Status.OK_STATUS;
					}
					UIUtility.syncExecInUIThread(new Runnable() {
						
						@Override
						public void run() {

							IStatusLineManager ex = plugin.getActivationContext().getService(IStatusLineManager.class);
							
							LinkStatusLineContributionItem link = new LinkStatusLineContributionItem("link");
							try {
								InetAddress adr = Inet4Address.getByName("localhost");
								//<a href=\"native\">access to the user-interface facilities of the operating systems</a>
								String hostadr = "<a href=\"http://"+adr.getHostAddress()+":8080/jester\">jester</a>";
								link.setLinkListener(new SelectionListener() {
									
									@Override
									public void widgetSelected(SelectionEvent e) {
										String nativeLink = e.text;
										URI uri;
										try {
											uri = new URI(nativeLink);
											Desktop.getDesktop().browse(uri);
										} catch (URISyntaxException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										} catch (IOException e0) {
											// TODO Auto-generated catch block
											e0.printStackTrace();
										}
									
										
										
									}
									
									@Override
									public void widgetDefaultSelected(SelectionEvent e) {
										// TODO Auto-generated method stub
										
									}
								});
								link.setText(hostadr);
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							ex.appendToGroup(StatusLineManager.END_GROUP, link);
							ex.update(true);
							
						}
					});

					return Status.OK_STATUS;
				}
				
			};
			job.schedule();
			
		}
		return settings;
	}
	
	public void stopDelegate(BundleContext context) {
		try {
			//JettyConfigurator.stopServer(PLUGIN_ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static OSGiGatewayActivator getDefault() {
		return plugin;
	}


}
