package ch.jester.ui.tournament.internal;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.osgi.framework.BundleContext;

import ch.jester.common.ui.activator.AbstractUIActivator;
import ch.jester.common.ui.editor.GenericDaoInputAccess;
import ch.jester.common.ui.services.IEditorService;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.util.RankingReportInput;
import ch.jester.ui.round.editors.RankingViewEditor;
import ch.jester.ui.round.editors.RoundEditor;
import ch.jester.ui.tournament.editors.TournamentEditor;
import ch.jester.ui.tournament.editors.WirePlayerEditor;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch.jester.ui.tournament"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private FormColors formColors;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	public void startDelegate(BundleContext context) {
		//Editor an InputTyp binden
		IEditorService openService = getActivationContext().getService(IEditorService.class);
		openService.register(Category.class, GenericDaoInputAccess.class, WirePlayerEditor.ID);
		openService.register(Tournament.class, GenericDaoInputAccess.class, TournamentEditor.ID);
		openService.register(Round.class, GenericDaoInputAccess.class, RoundEditor.ID);
		openService.register(Category.class, GenericDaoInputAccess.class, RoundEditor.ID);
		openService.register(RankingReportInput.class, GenericDaoInputAccess.class, RankingViewEditor.ID);
	}

	public void stopDelegate(BundleContext context) {
		if (formColors != null) {
			formColors.dispose();
			formColors = null;
		}
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public FormColors getFormColors(Display display) {
		if (formColors == null) {
			formColors = new FormColors(display);
			formColors.markShared();
		}
		return formColors;
	}
}
