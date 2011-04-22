package ch.jester.system.api.pairing;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.components.IComponentService;
import ch.jester.system.exceptions.NotAllResultsException;

public interface IPairingManager extends IComponentService<IPairingAlgorithm> {
	
	/**
	 * Ausführen der Paarungen
	 * @param progressMonitor
	 */
	public void doPairings(IProgressMonitor progressMonitor) throws NotAllResultsException;

}
