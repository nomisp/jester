package ch.jester.common.ui.activator;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.jester.common.activator.DefaultBundleActivatorContext;
import ch.jester.common.activator.IActivationContext;
import ch.jester.common.activator.IDelegationActivator;

public abstract class AbstractUIActivator extends AbstractUIPlugin implements IDelegationActivator<AbstractUIPlugin>{
	private IActivationContext<AbstractUIPlugin> mContext;
	@Override
	public IActivationContext<AbstractUIPlugin> getActivationContext() {
		return mContext;
	}
	@Override
	public final void start(BundleContext pContext) throws Exception {
		super.start(pContext);
		mContext = new DefaultBundleActivatorContext<AbstractUIPlugin>(pContext, this);
		startDelegate(pContext);
	}
	@Override
	public final void stop(BundleContext pContext) throws Exception {
		stopDelegate(pContext);
		mContext.getServiceUtil().closeAllTrackers();
		super.stop(pContext);
	}
	
}
