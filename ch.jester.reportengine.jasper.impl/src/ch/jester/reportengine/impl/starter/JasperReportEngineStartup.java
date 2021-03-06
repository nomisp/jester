package ch.jester.reportengine.impl.starter;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

import ch.jester.commonservices.api.reportengine.IReportEngine;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.reportengine.impl.Activator;

/**
 * Impl von {@link IStartup}... starten der Engine bei Appstart
 *
 */
public class JasperReportEngineStartup implements IStartup{

	@Override
	public void earlyStartup() {
		Job job = new Job(Messages.JasperReportEngineStartup_startup){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Activator.getDefault().exportModelForStudioOrReports();
				ServiceUtility su = new ServiceUtility();
				//Trigger ReportEngine Init & Report Compilation --> nicht unused!!!!
				@SuppressWarnings("unused")
				IReportEngine engine = su.getService(IReportEngine.class);
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
		
	}

}
