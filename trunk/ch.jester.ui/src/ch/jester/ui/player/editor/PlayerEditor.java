package ch.jester.ui.player.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.IDirtyListener;
import ch.jester.dao.IPlayerDao;
import ch.jester.ui.player.editor.ctrl.PlayerDetailsController;
import ch.jester.ui.player.editor.view.PlayerDetailsView;

public class PlayerEditor extends AbstractEditor{
	
	public static final String ID = "ch.jester.ui.player.editor.PlayerEditor"; //$NON-NLS-1$
	private PlayerInput mPlayerInput;
	private PlayerDetailsView mPlayerDetails;
	private PlayerDetailsController mPlayerDetailsController;
	private boolean saved;
	private IPlayerDao mDao;
	public PlayerEditor() {
		System.out.println("new player editor "+this);
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
		
		mPlayerDetailsController.getDirtyManager().setDirty(mPlayerInput.isDirty());
		
		
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

	public void setPlayerDao(IPlayerDao dao){
		System.out.println("setting dao"+this+" mDAO: "+dao);
		mDao=dao;
	}
	
	@Override
	public void editorClosed(){
		if(!saved){
			mPlayerDetailsController.getPlayer();
			
		}
		mPlayerDetailsController.updateUI();
	}

	@Override
	public void setFocus() {
		System.out.println("Editor has focus: "+this+" mDAO: "+mDao);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		System.out.println(this);
		saved = true;
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		try{
			mPlayerDetailsController.updateModel();
			mDao.save(mPlayerDetailsController.getPlayer());
			getDirtyManager().reset();
		}finally{
			//mDao.close();
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


		super.init(site, input);
	}
	
}
