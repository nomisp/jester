package ch.jester.common.ui.databinding;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.IAdaptable;

import ch.jester.common.utility.AdapterUtility;

public class PageControllerPropertyTester extends PropertyTester {

	public PageControllerPropertyTester() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if(!(receiver instanceof IAdaptable)){return false;}
		IAdaptable adaptable = (IAdaptable) receiver;
		PageController controller = AdapterUtility.getAdaptedObject(adaptable, PageController.class);
		if(controller==null){return false;}
		if(args[0].equals("next")){
			System.out.println("Controller next: "+controller.hasNextPage()+" receiver: "+receiver);
			return controller.hasNextPage();
		}
		if(args[0].equals("back")){
			System.out.println("Controller back: "+controller.hasPreviousPage()+" receiver: "+receiver);
			return controller.hasPreviousPage();
		}
		return false;
	}

}
