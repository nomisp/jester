package ch.jester.ui.round.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.progress.UIJob;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.reportengine.IReportResult.ExportType;
import ch.jester.ui.round.form.BrowserForm;
import ch.jester.ui.tournament.internal.Activator;
import ch.jester.ui.tournament.internal.RankingEntityHelp;
import ch.jester.ui.tournament.nl1.Messages;

/**
 * Turnier-Editor
 *
 */
public class RankingViewEditor extends AbstractEditor<RankingEntityHelp> {
	public static final String ID = "ch.jester.ui.tournament.resulteditor"; //$NON-NLS-1$
	private Semaphore sem = new Semaphore(1);
	private BrowserForm browserForm;
	private File tmpFile;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	public RankingViewEditor() {
		super(true);
		mLogger.debug("new "+this); //$NON-NLS-1$
	}

	
	public File getInputFile(){
		try {
			sem.acquire();
			sem.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
			return tmpFile;
	
	}

	@Override
	protected void addPages() {
		browserForm = new BrowserForm(this, "asdf", "ResultView");
		try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Job uijob = new Job("Generating RankingList") {
			
			@Override
			public IStatus run(IProgressMonitor monitor) {
				try{
					monitor.beginTask("Generating Rankinglist", IProgressMonitor.UNKNOWN);
					RankingEntityHelp hlp = mDaoInput.getInput();
					IReportEngine engine = getServiceUtil().getService(IReportEngine.class);
					IReport report = engine.getRepository().getReport("playerlist");
					List<Map<?,?>> list = new ArrayList<Map<?,?>>();
					list.add(hlp.getRankingInputMap());
					IReportResult result = engine.generate(report, list);
					tmpFile = result.export(ExportType.HTML);
					monitor.done();
				}catch(Exception e){
					
				}finally{
					sem.release();
				}
				return Status.OK_STATUS;
			}
		};
		uijob.setUser(true);
		uijob.schedule(0);
		
		//browserForm.setInput(tmpFile);
		
		try {
			addPage(browserForm);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	

	
	
	
	@Override
	public void dispose() {

		super.dispose();
	}
	@Override
	public void doSave(IProgressMonitor monitor) {
		mLogger.debug(Messages.RoundEditor_Saving+this);
		monitor.beginTask(Messages.RoundEditor_Saving, IProgressMonitor.UNKNOWN);
		try{

			setSaved(true);
		} finally {
			monitor.done();
		}
	}

}
