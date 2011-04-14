package ch.jester.common.components;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.osgi.service.component.ComponentContext;


import ch.jester.commonservices.api.bundle.IActivationContext;
import ch.jester.commonservices.api.components.IEPEntry;
import ch.jester.commonservices.api.components.IEPEntryComponentService;
//import ch.jester.commonservices.api.components.IEPService;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.ep.ExtensionPointChangeNotifier;

/**
 * Abstrakte Komponente welche, auf einen ExtensionPoint horcht und die daran
 * registrierten Services als Proxies bei sich registriert
 *
 * @param <V> der EntryTyp
 * @param <T> der Handler
 */
public abstract class AbstractEPComponent<V extends IEPEntry<T>, T> implements IEPEntryComponentService<V, T> {
	private Object mLock=new Object();
	private ComponentContext mContext;
	private ILogger mLogger;
	private IActivationContext<?> mActivationContext;
	private ExtensionPointChangeNotifier mEPNotifier;
	private Class<T> mClassType;
	private HashMap<V,T> mImportHandlers = new HashMap<V,T>();
	public AbstractEPComponent(Class<T> classType, IActivationContext<?> pActivationContext, String pEPId, String pEPName){
		mActivationContext=pActivationContext;
		mClassType=classType;
		mLogger = mActivationContext.getLogger();
		mLogger.debug("Starting Component: "+AbstractEPComponent.this);
		mEPNotifier = new ExtensionPointChangeNotifier(pEPId,pEPName){
			HashMap<IConfigurationElement, T> mHandlerMap = new HashMap<IConfigurationElement, T>();
			protected void added(IConfigurationElement iConfigurationElement) {
				T e = createProxy(iConfigurationElement);
				mHandlerMap.put(iConfigurationElement, e);
				bind(e);
			};
			protected void removed(Object object) {
				mHandlerMap.remove(object);
				unbind(mHandlerMap.remove(object));
				
			};
			
		};
		mEPNotifier.start();
		
	}
	
	/**Definiert das ExecutableElement im IConfigurationElement, welches
	 * benutzt werden soll, um im Proxy eine Instanz zu erzeugen.
	 * Default: class 
	 *
	 * @return
	 */
	protected String getClassAttribute(){
		return "class";
	}

	@SuppressWarnings("unchecked")
	protected T createProxy(IConfigurationElement pConfigurationElement){
		return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mClassType, IEPEntry.class}, new EPServiceProxy<T>(pConfigurationElement, mClassType, getClassAttribute()));
	}
	

	@Override
	public List<V> getRegistredEntries() {
		synchronized(mLock){
			return new ArrayList<V>(mImportHandlers.keySet());
		}
	}
	
	@Override
	public void start(ComponentContext pComponentContext) {
		mContext=pComponentContext;
		mLogger.debug("Start Context: "+mContext);
	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		mLogger.debug("Stop Context: "+mContext);
		
	}

	@Override
	public void bind(T pT) {
		mLogger.debug("bind Object: "+pT);
		synchronized(mLock){
			addToList(pT);
		}
		
	}

	@Override
	public void unbind(T pT) {
		mLogger.debug("unbind Object: "+pT);
		synchronized(mLock){
			removeFromList(pT);
		}
		
		
	}
	
	private void removeFromList(T o) {
		mImportHandlers.remove(o);
		
	}

	private void addToList(T o) {
		V entry = createEntry(o);
		
		mImportHandlers.put(entry,o);
	}

	/**
	 * Template Methode die ein konkretes Objekt des angegeben Types T verlangt
	 * @param o
	 * @return
	 */
	protected abstract V createEntry(T o);

}
