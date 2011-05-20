package ch.jester.ui.tournament.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.model.Category;
import ch.jester.ui.tournament.ctrl.WirePlayerController;
import ch.jester.ui.tournament.forms.WirePlayerForm;

public class WirePlayerEditor extends AbstractEditor<Category> {
	
	public static final String ID = "ch.jester.ui.tournament.wirePlayerEditor";
	private WirePlayerForm wirePlayerFormPage;
	private WirePlayerController wirePlayerController;

	public WirePlayerEditor() {
		super(false);
		mLogger.debug("Wire Player to Category Editor " + this);
	}

	@Override
	protected void addPages() {
		WirePlayerForm wirePlayerForm = new WirePlayerForm(this, "WirePlayerForm", "WPEditor");
		try {
			super.addPage(wirePlayerForm);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

}
