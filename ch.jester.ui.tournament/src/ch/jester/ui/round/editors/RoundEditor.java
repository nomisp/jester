package ch.jester.ui.round.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.ui.round.form.BrowserForm;
import ch.jester.ui.round.form.ResultForm;
import ch.jester.ui.round.form.RoundForm;
import ch.jester.ui.round.form.contentprovider.CategoryNodeModelContentProvider;
import ch.jester.ui.round.form.contentprovider.RoundNodeModelContentProvider;
import ch.jester.ui.tournament.internal.Activator;
import ch.jester.ui.tournament.nl1.Messages;

/**
 * Turnier-Editor
 *
 */
public class RoundEditor extends AbstractEditor<IEntityObject> {
	public static final String ID = "ch.jester.ui.tournament.roundeditor"; //$NON-NLS-1$
	private ResultForm tableResultFormPage;
	private RoundForm graphResultFormPage;

	private ResultController mController = new ResultController();
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	public RoundEditor() {
		super(true);
		mLogger.debug("new "+this); //$NON-NLS-1$
	}


	@Override
	protected void addPages() {
		mController.setInput(mDaoInput.getInput());
		
		
		
		graphResultFormPage = new RoundForm(this, "roundeditor", Messages.RoundEditor_Graph); //$NON-NLS-1$
		graphResultFormPage.setResultController(mController);
		
		tableResultFormPage = new ResultForm(this, "resultform", Messages.RoundEditor_Table); //$NON-NLS-1$
		tableResultFormPage.setResultController(mController);
	
		graphResultFormPage.setContentProvider(getContentProvider(mDaoInput.getInput()));
		
		setDirtyManager(tableResultFormPage.getDirtyManager());
		getDirtyManager().addDirtyListener(this);
		
		try {
			addPage(graphResultFormPage);
			addPage(tableResultFormPage);
			addPage(new BrowserForm(this, "", "Browser"));
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	private RoundNodeModelContentProvider getContentProvider(Object o){
		if(o instanceof Round){
			RoundNodeModelContentProvider prov = new RoundNodeModelContentProvider(mController);
			prov.setInput(o);
			return prov;
		}
		if(o instanceof Category){
			CategoryNodeModelContentProvider prov = new CategoryNodeModelContentProvider(mController);
			prov.setInput(o);
			return prov;
		}
		
		return null;
	}
	
	public void init_0(Object parent) {
		/*mTournamentPage = (TournamentFormPage) parent;
		mTournamentController = mTournamentPage.getController();			
		mTournamentController.setTournament(mDaoInput.getInput());	
		mTournamentController.getDirtyManager().setDirty(mDaoInput.isAlreadyDirty());
		setDaoService(super.getServiceUtil().getDaoServiceByEntity(Tournament.class));
		
		setDirtyManager(mTournamentController.getDirtyManager());
		getDirtyManager().addDirtyListener(this);
		mTournamentController.getDirtyManager().addDirtyListener(new IDirtyListener() {
			@Override
			public void propertyIsDirty() {
				setPartName(mTournamentPage.getNameText().getText());
				
			}
		});
		setPartName(mTournamentPage.getNameText().getText()+", "+mTournamentPage.getDescriptionText().getText());*/
	
	}
	
	@Override
	public void dispose() {
		mController.dispose();
		super.dispose();
	}
	@Override
	public void doSave(IProgressMonitor monitor) {
		mLogger.debug(Messages.RoundEditor_Saving+this);
		monitor.beginTask(Messages.RoundEditor_Saving, IProgressMonitor.UNKNOWN);
		try{
			mController.saveChangedResults();
			setSaved(true);
		} finally {
			monitor.done();
		}
	}

}
