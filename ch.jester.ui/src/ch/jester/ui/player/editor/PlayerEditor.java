package ch.jester.ui.player.editor;

import java.beans.PropertyChangeEvent;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.IDirtyManagerPropertyInvoker;
import ch.jester.common.ui.editorutilities.SitePartNameHandler;
import ch.jester.dao.IPlayerDao;
import ch.jester.ui.player.editor.ctrl.PlayerDetailsController;
import ch.jester.ui.player.editor.view.PlayerDetailsView;

public class PlayerEditor extends AbstractEditor{
	
	public static final String ID = "ch.jester.ui.player.editor.PlayerEditor"; //$NON-NLS-1$
	private PlayerInput mPlayerInput;
	private PlayerDetailsView mPlayerDetails;
	private PlayerDetailsController mPlayerDetailsController;
	private boolean saved;
	public PlayerEditor() {

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
		
		
		mPlayerDetailsController.setPlayer(mPlayerInput.getPlayer());
		
		setDirtyManager(mPlayerDetailsController.getDirtyManager());
		getDirtyManager().addDirtyManagerPropertyInvoker(this);
		
		mPlayerDetailsController.getDirtyManager().addDirtyManagerPropertyInvoker(new IDirtyManagerPropertyInvoker() {
			
			@Override
			public void fireDirtyProperty() {
				setPartName(mPlayerDetails.getLastNameText().getText()+", "+mPlayerDetails.getFirstNameText().getText());
				
			}
		});
		setPartName(mPlayerDetails.getLastNameText().getText()+", "+mPlayerDetails.getFirstNameText().getText());
	
	}

	@Override
	public void editorClosed(){
		if(!saved){
			mPlayerDetailsController.getPlayer();
			
		}
		mPlayerDetailsController.updateUI();
	}


	@Override
	public void doSave(IProgressMonitor monitor) {
		saved = true;
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		IPlayerDao persister = getServiceUtil().getExclusiveService(IPlayerDao.class);
		try{
			mPlayerDetailsController.updateModel();
			persister.save(mPlayerDetailsController.getPlayer());
			getDirtyManager().reset();
		}finally{
			persister.close();
			monitor.done();
		}

	}

	public boolean wasSaved(){
		return saved;
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		
		this.mPlayerInput = (PlayerInput) input;
		setSite(site);
		setInput(input);

		/*SitePartNameHandler partNameHandler = new SitePartNameHandler() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				setPartName(mPlayerInput.getPlayer().getLastName()+", "+mPlayerInput.getPlayer().getFirstName());	
			}
		};*/

		super.init(site, input);
	}
	
}
