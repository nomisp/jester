package ch.jester.ui.player.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.model.Player;
import ch.jester.ui.player.editor.ctrl.PlayerDetailsController;
import ch.jester.ui.player.editor.view.PlayerDetailsView;

public class PlayerEditor extends AbstractEditor<Player>{
	
	public static final String ID = "ch.jester.ui.player.editor.PlayerEditor"; //$NON-NLS-1$

	private PlayerDetailsView mPlayerDetails;
	private PlayerDetailsController mPlayerDetailsController;
	public PlayerEditor() {
		mLogger.debug("New player editor "+this);
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		mPlayerDetails = new PlayerDetailsView(container, SWT.NONE);
		mPlayerDetails.setBounds(0, 0, 365, 300);
		
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

	@Override
	public void setFocus() {
		mPlayerDetails.setFocus();
	}

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
	
	
	
	
}
