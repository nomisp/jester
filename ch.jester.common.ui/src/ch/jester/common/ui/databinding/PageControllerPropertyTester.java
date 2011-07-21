package ch.jester.common.ui.databinding;

import org.eclipse.core.expressions.PropertyTester;

import ch.jester.common.ui.internal.CommonUIActivator;
import ch.jester.commonservices.api.logging.ILogger;

public class PageControllerPropertyTester extends PropertyTester {
	private ILogger mLogger;
	public PageControllerPropertyTester() {
		mLogger = CommonUIActivator.getDefault().getActivationContext().getLogger();
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		PageController<?> controller = (PageController<?>) receiver;
		if(controller==null){return false;}
		boolean result = false;
		if(args[0].equals("next")){
			result = controller.hasNextPage();
			//mLogger.debug("Controller Property 'next' is " + result + " for receiver " +receiver );
			return result;
		}
		if(args[0].equals("back")){
			result = controller.hasPreviousPage();
			//mLogger.debug("Controller Property 'back' is " + result + " for receiver " +receiver );
			return result;
		}
		mLogger.debug("Controller Property could not be evaluated, due to wrong argument");
		return false;
	}

}
