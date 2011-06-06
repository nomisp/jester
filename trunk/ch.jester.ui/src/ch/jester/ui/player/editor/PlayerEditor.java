package ch.jester.ui.player.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.model.Player;
import ch.jester.ui.forms.PlayerFormPage;
import ch.jester.ui.player.editor.ctrl.PlayerDetailsController;

public class PlayerEditor extends AbstractEditor<Player>{
	
	public static final String ID = "ch.jester.ui.player.editor.PlayerEditor"; //$NON-NLS-1$

	private PlayerFormPage mPlayerDetails;
	private PlayerDetailsController mPlayerDetailsController;
	public PlayerEditor() {
		super(false);
		mLogger.debug("New player editor "+this);
	}


	public void init_0(Object parent) {
		//Composite container = new Composite(parent, SWT.NONE);
		
		//mPlayerDetails = new PlayerDetailsView(container, SWT.NONE);
		//mPlayerDetails.setBounds(0, 0, 365, 300);
		mPlayerDetails = (PlayerFormPage) parent;
		mPlayerDetailsController = mPlayerDetails.getController();			
		mPlayerDetailsController.setPlayer(mDaoInput.getInput());	
		mPlayerDetailsController.getDirtyManager().setDirty(mDaoInput.isAlreadyDirty());
		
		
		setDirtyManager(mPlayerDetailsController.getDirtyManager());
		getDirtyManager().addDirtyListener(this);
		mPlayerDetailsController.getDirtyManager().addDirtyListener(new IDirtyListener() {
			@Override
			public void propertyIsDirty() {
				setPartName(mPlayerDetails.getLastNameText().getText()+", "+mPlayerDetails.getFirstNameText().getText());
				
			}
		});
		setPartName(mPlayerDetails.getLastNameText().getText()+", "+mPlayerDetails.getFirstNameText().getText());
	
	}


	
	@Override
	public void editorClosed(){
		if(!wasSaved()){
			mPlayerDetailsController.getPlayer();
			
		}
		mPlayerDetailsController.updateUI();
	}

/*	@Override
	public void setFocus() {
		int i = getActivePage();
		getPageSite(i).
		mPlayerDetails.setFocus();
	}*/

	@Override
	public void doSave(IProgressMonitor monitor) {
		mLogger.debug("Saving "+this);
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		try{
			mPlayerDetailsController.updateModel();
			mDao.save(mPlayerDetailsController.getPlayer());
			getDirtyManager().reset();
			setSaved(true);
		}finally{
			monitor.done();
		}

	}

	@Override
	protected void addPages() {
		PlayerFormPage pfp = new PlayerFormPage(this, "PlayerFormPage", "PEditor");
		pfp.addPartPropertyListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				init_0(null);
				
			}
		});
		try {
			super.addPage(pfp);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
