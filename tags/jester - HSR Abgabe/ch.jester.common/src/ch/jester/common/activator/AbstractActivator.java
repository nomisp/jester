package ch.jester.common.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ch.jester.common.utility.CallerUtility;
import ch.jester.common.utility.CallerUtility.Caller;
import ch.jester.commonservices.api.bundle.IActivationContext;
import ch.jester.commonservices.api.bundle.IDelegationActivator;

/**
 * Abstrakter Aktivator der {@link IDelegationActivator} implementiert.
 *
 */
public abstract class AbstractActivator implements IDelegationActivator<BundleActivator>{
	private IActivationContext<BundleActivator> mContext;
	@Override
	public IActivationContext<BundleActivator> getActivationContext() {
		if(mContext!=null){return mContext;}
		Caller caller = CallerUtility.getCaller(2);
		String method = caller.getCallerMethod();
		String callingClass = caller.getCallerClass();
		String selfClass =  this.getClass().getName();
		if(callingClass==selfClass && method.equals("<init>")){
			throw new IllegalAccessError("getActivationContext can't be called during Initialization");
		}
		return mContext;
	}


	@Override
	public final void start(BundleContext pContext) throws Exception {
		mContext = new DefaultBundleActivatorContext<BundleActivator>(pContext, this);
		startDelegate(pContext);
	}

	@Override
	public final void stop(BundleContext pContext) throws Exception {
		mContext.getServiceUtil().closeAllTrackers();
		stopDelegate(pContext);
	}
	

}
