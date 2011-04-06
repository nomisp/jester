package ch.jester.ui.player.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;


import ch.jester.common.utility.ServiceUtility;
import ch.jester.dao.IPlayerPersister;
import ch.jester.ui.Activator;
import ch.jester.ui.editor.utilities.DirtyManagerPropertyInvoker;

public class PlayerEditor extends EditorPart{

	public static final String ID = "ch.jester.ui.player.editor.PlayerEditor"; //$NON-NLS-1$
	private PlayerInput mPlayerInput;
	private PlayerDetails mPlayerDetails;
	private ServiceUtility mServices;
	public PlayerEditor() {
		mServices = Activator.getDefault().getActivationContext().getServiceUtil();
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
		mPlayerDetails.getController().getDirtyManager().setDirtyManagerPropertyInvoker(new DirtyManagerPropertyInvoker() {	
			@Override
			public void fireDirtyProperty() {
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		});
		initDataBindings();
		
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		IPlayerPersister persister = mServices.getExclusiveService(IPlayerPersister.class);
		try{
			persister.save(mPlayerDetails.getController().getPlayer());
			mPlayerDetails.getController().getDirtyManager().reset();
		}finally{
			persister.close();
			monitor.done();
		}

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
	}

	@Override
	public boolean isDirty() {
		return mPlayerDetails.getController().getDirtyManager().isDirty();
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
		mPlayerDetails.getController().dispose();
		super.dispose();
	}
	
}
