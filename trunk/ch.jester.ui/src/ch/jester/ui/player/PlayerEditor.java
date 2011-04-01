package ch.jester.ui.player;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import ch.jester.model.Player;
import ch.jester.ui.handlers.PlayerInput;

public class PlayerEditor extends EditorPart {

	public static final String ID = "ch.jester.ui.player.PlayerEditor"; //$NON-NLS-1$
	private PlayerInput input;
	private Player person;

	public PlayerEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		PlayerDetails playerDetails = new PlayerDetails(container, SWT.NONE);
		playerDetails.setBounds(0, 0, 365, 300);

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		PlayerInput new_name = (PlayerInput) input;
		this.input = (PlayerInput) input;
		setSite(site);
		setInput(input);
		person = new_name.getPlayer();
		setPartName("Person " + person.getLastName());

	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
