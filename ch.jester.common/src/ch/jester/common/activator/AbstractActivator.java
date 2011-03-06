package ch.jester.common.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public abstract class AbstractActivator implements IDelegationActivator<BundleActivator>{
	private IActivationContext<BundleActivator> mContext;
	@Override
	public IActivationContext<BundleActivator> getActivationContext() {
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
