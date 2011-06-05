package ch.jester.ui.handlers;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.ui.utility.SafeMessageBoxRunner;
import ch.jester.commonservices.api.reportengine.IReport;
import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.api.reportengine.IReportResult;
import ch.jester.commonservices.api.reportengine.IReportResult.ExportType;

public class PrintHandler extends AbstractCommandHandler {

	@Override
	public Object executeInternal(ExecutionEvent event) {
		final List<?> selectedBeans = mSelUtility.getAsStructuredSelection().toList();
		
		final IReportEngine engine = getServiceUtil().getService(IReportEngine.class);
		final IReport report = engine.getFactory().getReport("playerlist");
		
		
		Job job = new Job("Printing"){

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				SafeRunner.run(new SafeMessageBoxRunner() {
					
					@Override
					public void run() throws Exception {
						monitor.beginTask("Preparing...", IProgressMonitor.UNKNOWN);
						
						monitor.worked(1);
						IReportResult result = engine.generate(report, selectedBeans);
						
						monitor.setTaskName("Exporting...");
						monitor.worked(1);
						result.setOutputName("test");
						
						result.export(ExportType.PDF);
						
						Desktop.getDesktop().open(new File("test.pdf"));
						monitor.done();
						
					}
				});
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
		
		return null;
	}

	
}
