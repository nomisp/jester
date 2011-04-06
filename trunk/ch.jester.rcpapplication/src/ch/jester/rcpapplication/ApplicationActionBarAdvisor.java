package ch.jester.rcpapplication;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}
	
	protected void makeActions(IWorkbenchWindow window) {
		IWorkbenchAction action = ActionFactory.HELP_CONTENTS.create(window);
		this.register(action);
		action = ActionFactory.HELP_SEARCH.create(window);
		this.register(action);
		action = ActionFactory.DYNAMIC_HELP.create(window);
		this.register(action);
		action = ActionFactory.SAVE.create(window);
		this.register(action);
		action = ActionFactory.SAVE_ALL.create(window);
		this.register(action);
	}
}
