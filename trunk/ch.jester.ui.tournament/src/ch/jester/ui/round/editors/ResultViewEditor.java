package ch.jester.ui.round.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;

import ch.jester.common.ui.editor.AbstractEditor;
import ch.jester.commonservices.api.io.IFileManager;
import ch.jester.commonservices.api.logging.ILogger;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportRepository;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.reportengine.IReportResult.ExportType;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.ui.round.form.BrowserForm;
import ch.jester.ui.round.form.ResultForm;
import ch.jester.ui.round.form.RoundForm;
import ch.jester.ui.round.form.contentprovider.CategoryNodeModelContentProvider;
import ch.jester.ui.round.form.contentprovider.RoundNodeModelContentProvider;
import ch.jester.ui.tournament.internal.Activator;
import ch.jester.ui.tournament.internal.RankingEntityHelp;
import ch.jester.ui.tournament.nl1.Messages;

/**
 * Turnier-Editor
 *
 */
public class ResultViewEditor extends AbstractEditor<RankingEntityHelp> {
	public static final String ID = "ch.jester.ui.tournament.resulteditor"; //$NON-NLS-1$

	private BrowserForm browserForm;
	private File tmpFile;
	private ILogger mLogger = Activator.getDefault().getActivationContext().getLogger();
	public ResultViewEditor() {
		super(true);
		mLogger.debug("new "+this); //$NON-NLS-1$
	}

	
	public File getInputFile(){
		return tmpFile;
	}

	@Override
	protected void addPages() {

		browserForm = new BrowserForm(this, "asdf", "ResultView");
		
		RankingEntityHelp hlp = mDaoInput.getInput();
		IReportEngine engine = super.getServiceUtil().getService(IReportEngine.class);
		IReport report = engine.getRepository().getReport("playerlist");
		IReportResult result = engine.generate(report, hlp.getPlayerInputListForTest());
		tmpFile = result.export(ExportType.HTML);
		
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
