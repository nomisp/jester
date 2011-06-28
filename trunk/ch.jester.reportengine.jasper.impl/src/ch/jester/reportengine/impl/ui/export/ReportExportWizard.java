package ch.jester.reportengine.impl.ui.export;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Tournament;


public class ReportExportWizard extends Wizard implements IExportWizard {

	ReportExportPage firstPage = new ReportExportPage();
	public ReportExportWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.setNeedsProgressMonitor(true);

	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(firstPage);

	}
	@Override
	public boolean canFinish() {
		return firstPage.valid();
	}

	@Override
	public boolean performFinish() {
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					try{
					ServiceUtility su = new ServiceUtility();
					File outputFile = new File(firstPage.getSelectedFile());
					IReportEngine engine = su.getService(IReportEngine.class);
					List<Tournament> input = new ArrayList<Tournament>();
					input.add(firstPage.getSelectedTournament());
					IReportResult result = engine.generate(firstPage.getSelectedReport(), input);
					FileOutputStream fos;
					result.export(firstPage.getSelectedExportType(), fos = new FileOutputStream(outputFile));
					fos.close();
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


}
