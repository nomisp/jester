package ch.jester.importmanagerservice.impl.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.osgi.service.component.ComponentContext;

import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.ep.ExtensionPointChangeNotifier;

public class DefaultImportManager implements IImportManager{
	private Object mLock=new Object();
	private ComponentContext mContext;
	private ILogger mLogger;
	private ImportManagerActivator mActivator;
	private HashMap<IImportHandler,IImportHandlerEntry<IImportHandler>> mImportHandlers = new HashMap<IImportHandler,IImportHandlerEntry<IImportHandler>>();
	private ExtensionPointChangeNotifier notifier;
	public DefaultImportManager(){
		mActivator=ImportManagerActivator.getInstance();
		mLogger = mActivator.getActivationContext().getLogger();
		mLogger.info("Starting ImportManager Component");
		notifier = new ExtensionPointChangeNotifier("ch.jester.commonservices.api","ImportHandler"){
			HashMap<IConfigurationElement, ExtensionPointImportHandler> mHandlerMap = new HashMap<IConfigurationElement, ExtensionPointImportHandler>();
			protected void added(IConfigurationElement iConfigurationElement) {
				ExtensionPointImportHandler e = new ExtensionPointImportHandler(iConfigurationElement);
				mHandlerMap.put(iConfigurationElement, e);
				bind(e);
			};
			protected void removed(Object object) {
				unbind(mHandlerMap.remove(object));
				
			};
			
		};
		notifier.start();
	}
	
	@Override
	public List<IImportHandlerEntry<IImportHandler>> getRegistredImportHandlers() {
		synchronized(mLock){
			return new ArrayList<IImportHandlerEntry<IImportHandler>>(mImportHandlers.values());
		}
	}

	@Override
	public Object doImport(IImportHandlerEntry<IImportHandler> pEntry,
			Object pObjectToImport) {
		return null;
	}

	@Override
	public void start(ComponentContext o) {
		mContext=o;
		mLogger.debug("start Context: "+mContext);
		
	}

	@Override
	public void stop(ComponentContext o) {
		mLogger.debug("stop Context: "+mContext);
		
	}
	@Override
	public  void bind(IImportHandler o) {
		mLogger.debug("bind Object: "+o);
		synchronized(mLock){
			addToList(o);
		}
	}

	@Override
	public void unbind(IImportHandler o) {
		mLogger.debug("unbind Object: "+o);
		synchronized(mLock){
			removeFromList(o);
		}
		
	}

	private void removeFromList(IImportHandler o) {
		mImportHandlers.remove(o);
		
	}

	public void addToList(IImportHandler o) {
		IImportHandlerEntry<IImportHandler> entry = new DefaultImportHandlerEntry<IImportHandler>(o);
		mImportHandlers.put(o, entry);
	}


}
