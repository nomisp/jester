package ch.jester.ui.player.editor;

import java.beans.PropertyChangeEvent;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.common.ui.editorutilities.SitePartNameHandler;
import ch.jester.dao.IPlayerDao;

public class PlayerEditor extends AbstractEditor{

	public static final String ID = "ch.jester.ui.player.editor.PlayerEditor"; //$NON-NLS-1$
	private PlayerInput mPlayerInput;
	private PlayerDetails mPlayerDetails;
	private PlayerDetailsController mPlayerDetailsController;
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
		mPlayerDetailsController = mPlayerDetails.getController();
		
		
		mPlayerDetailsController.setPlayer(mPlayerInput.getPlayer());
		
		setDirtyManager(mPlayerDetailsController.getDirtyManager());
		getDirtyManager().setDirtyManagerPropertyInvoker(this);
	}



	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		IPlayerDao persister = getServiceUtil().getExclusiveService(IPlayerDao.class);
		try{
			persister.save(mPlayerDetailsController.getPlayer());
			getDirtyManager().reset();
		}finally{
			persister.close();
			monitor.done();
		}

	}

	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.mPlayerInput = (PlayerInput) input;
		setSite(site);
		setInput(input);

		SitePartNameHandler partNameHandler = new SitePartNameHandler() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				setPartName(mPlayerInput.getPlayer().getLastName()+", "+mPlayerInput.getPlayer().getFirstName());	
			}
		};
		mPlayerInput.getPlayer().addPropertyChangeListener(partNameHandler);
		partNameHandler.init();
	}
	
	

	@Override
	public void dispose() {
		mPlayerDetailsController.dispose();
		super.dispose();
	}
}
