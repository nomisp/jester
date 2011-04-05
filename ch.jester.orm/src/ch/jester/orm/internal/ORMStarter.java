package ch.jester.orm.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

import ch.jester.orm.ORMPlugin;

/**
 * Implementiert IStartup.
 * Dadurch wird die EntityManagerFactory beim Starten generiert
 *
 */
public class ORMStarter implements IStartup{

	@Override
	public void earlyStartup() {
		//initialisierung
		Job job = new Job("Initialize DB Stuff"){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("setting up", IProgressMonitor.UNKNOWN);
				ORMPlugin.getJPAEntitManagerFactor();
				monitor.done();
				return Status.OK_STATUS;
			}
			
		};
		job.schedule();
	}

}
