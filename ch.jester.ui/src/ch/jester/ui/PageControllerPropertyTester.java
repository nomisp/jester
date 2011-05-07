package ch.jester.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.IAdaptable;

import ch.jester.common.utility.AdapterUtility;
import ch.jester.ui.contentprovider.PageController;

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
			System.out.println("Controller next: "+controller.hasNextPage());
			return controller.hasNextPage();
		}
		if(args[0].equals("back")){
			System.out.println("Controller back: "+controller.hasPreviousPage());
			return controller.hasPreviousPage();
		}
		return false;
	}

}
