package ch.jester.common.importer;

import java.util.Collection;
import java.util.HashMap;

import org.osgi.service.component.ComponentContext;

import ch.jester.common.activator.internal.CommonActivator;
import ch.jester.commonservices.api.importer.IImportHandler;
import ch.jester.commonservices.api.importer.IImportHandlerEntry;
import ch.jester.commonservices.api.importer.IImportManager;
import ch.jester.commonservices.api.logging.ILogger;

public class DefaultImportManager implements IImportManager{
	private Object mLock=new Object();
	private ComponentContext mContext;
	private ILogger mLogger;
	private HashMap<IImportHandler,IImportHandlerEntry<?>> mImportHandlers = new HashMap<IImportHandler,IImportHandlerEntry<?>>();
	public DefaultImportManager(){
		mLogger = CommonActivator.getInstance().getActivationContext().getLogger();
		mLogger.info("Starting ImportManager Component");

		
	}
	
	@Override
	public Collection<IImportHandlerEntry<?>> getRegistredImportHandlers() {
		synchronized(mLock){
			return mImportHandlers.values();
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
		IImportHandlerEntry<? extends IImportHandler> entry = new DefaultImportHandlerEntry<IImportHandler>(null, o);
		mImportHandlers.put(o, entry);
	}


}
