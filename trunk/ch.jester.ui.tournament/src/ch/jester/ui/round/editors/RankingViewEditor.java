package ch.jester.ui.round.editors;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.reportengine.IReportResult.ExportType;
import ch.jester.commonservices.exceptions.ProcessingException;
import ch.jester.model.util.RankingReportInput;
import ch.jester.ui.round.form.BrowserForm;
import ch.jester.ui.tournament.internal.Activator;
import ch.jester.ui.tournament.nl1.Messages;

/**
 * Turnier-Editor
 *
 */
public class RankingViewEditor extends AbstractEditor<RankingReportInput> {
	public static final String ID = "ch.jester.ui.tournament.resulteditor"; //$NON-NLS-1$
	private Semaphore sem = new Semaphore(1);
	private BrowserForm browserForm;
	private File tmpFile;
	private IReportResult result;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	public RankingViewEditor() {
		super(false);
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
					RankingReportInput hlp = mDaoInput.getInput();
					IReportEngine engine = getServiceUtil().getService(IReportEngine.class);
					IReport report = engine.getRepository().getReport("rankinglist");
					List<RankingReportInput> list = new ArrayList<RankingReportInput>();
					list.add(hlp);
					result = engine.generate(report, list, monitor);
					tmpFile = result.export(ExportType.HTML);
					browserForm.setInput(tmpFile);
					monitor.done();
				}catch(ProcessingException e){
					throw e;
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


	public void convert2PDF() {
		File pdf = result.export(ExportType.PDF);
		try {
			Desktop.getDesktop().open(pdf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void toPrinter() {
		File pdf = result.export(ExportType.PDF);
		try {
			Desktop.getDesktop().print(pdf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
