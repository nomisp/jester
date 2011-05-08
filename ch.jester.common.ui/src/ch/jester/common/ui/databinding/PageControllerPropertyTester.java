package ch.jester.common.ui.databinding;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.IAdaptable;

import ch.jester.common.ui.internal.Activator;
import ch.jester.common.utility.AdapterUtility;
import ch.jester.commonservices.api.logging.ILogger;

public class PageControllerPropertyTester extends PropertyTester {
	private ILogger mLogger;
	public PageControllerPropertyTester() {
		mLogger = Activator.getDefault().getActivationContext().getLogger();
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if(!(receiver instanceof IAdaptable)){return false;}
		IAdaptable adaptable = (IAdaptable) receiver;
		PageController<?> controller = AdapterUtility.getAdaptedObject(adaptable, PageController.class);
		if(controller==null){return false;}
		if(args[0].equals("next")){
			mLogger.debug("Controller Property 'next' is " + controller.hasNextPage() + " for receiver " +receiver );
			return controller.hasNextPage();
		}
		if(args[0].equals("back")){
			mLogger.debug("Controller Property 'back' is " + controller.hasNextPage() + " for receiver " +receiver );
			return controller.hasPreviousPage();
		}
		mLogger.debug("Controller Property could not be evaluated, due to wrong argument");
		return false;
	}

}
