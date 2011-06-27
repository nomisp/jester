package ch.jester.common.components;

import org.osgi.service.component.ComponentContext;

import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.commonservices.api.logging.ILogger;

/**
 * IComponent Service Adapter stellt eine uninitialisiertes Attribut ILogger mLogger zur Verfügung
 * @param  <T >
 */
public class ComponentAdapter<T> implements IComponentService<T>{
	/**
	 * @uml.property  name="mLogger"
	 * @uml.associationEnd  
	 */
	private ILogger mLogger;
	private ComponentContext mContext;
	@Override
	public void start(ComponentContext pComponentContext) {

	}

	@Override
	public void stop(ComponentContext pComponentContext) {
		
	}
	
	public ComponentContext getContext(){
		return mContext;
	}

	@Override
	public void bind(T pT) {

	}

	@Override
	public void unbind(T pT) {

	}

	protected ILogger getLogger(){
		return mLogger;
	}
	
	protected void setLogger(ILogger pLogger){
		mLogger = pLogger;
	}
	
	
}
