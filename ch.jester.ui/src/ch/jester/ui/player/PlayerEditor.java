package ch.jester.ui.player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import ch.jester.ui.handlers.PlayerInput;

public class PlayerEditor extends EditorPart {

	public static final String ID = "ch.jester.ui.player.PlayerEditor"; //$NON-NLS-1$
	private PlayerInput mPlayerInput;
	//private Player mPlayer;
	private PlayerDetails mPlayerDetails;
	private DirtyManager mDm = new DirtyManager();
	public PlayerEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		mPlayerDetails = new PlayerDetails(container, SWT.NONE);
		mPlayerDetails.setBounds(0, 0, 365, 300);
		mPlayerDetails.getController().setPlayer(mPlayerInput.getPlayer());
		initDataBindings();
		
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
		this.mPlayerInput = (PlayerInput) input;
		setSite(site);
		setInput(input);
		setPartName(mPlayerInput.getPlayer().getLastName()+", "+mPlayerInput.getPlayer().getFirstName());
		mPlayerInput.getPlayer().addPropertyChangeListener(new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				if(arg0.getPropertyName().equals("lastName")||arg0.getPropertyName().equals("firstName")){
					setPartName(mPlayerInput.getPlayer().getLastName()+", "+mPlayerInput.getPlayer().getFirstName());
				}
				
			}
			
		});
		mPlayerInput.getInput().addPropertyChangeListener(mDm);
	}

	@Override
	public boolean isDirty() {
		return mDm.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
	@Override
	public void dispose() {
		mPlayerInput.getInput().removePropertyChangeListener(mDm);
		super.dispose();
	}
	
	class DirtyManager implements PropertyChangeListener{
		boolean dirty;
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			dirty = true;
		}
		
		public boolean isDirty(){
			return dirty;
		}
		public void reset(){
			dirty=false;
		}
		
	}
}
