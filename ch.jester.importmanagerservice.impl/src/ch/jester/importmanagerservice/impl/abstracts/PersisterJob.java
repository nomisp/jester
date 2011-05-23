package ch.jester.importmanagerservice.impl.abstracts;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.jester.common.utility.StopWatch;
import ch.jester.common.utility.SubListIterator;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;

public class PersisterJob extends Job{	
	List<Player> pList;
	private ServiceUtility su = new ServiceUtility();
	public PersisterJob(String name, List<Player> players) {
		super(name);
		pList=players;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		StopWatch watch = new StopWatch();
		watch.start();
		
		SubListIterator<Player> iterator = new SubListIterator<Player>(pList, 10000);
		
		while(iterator.hasNext()){
			List<Player> sublist = iterator.next();
			IPlayerDao playerpersister = su.getExclusiveService(IPlayerDao.class);
			playerpersister.save(sublist);
			playerpersister.close();
		}
		watch.stop();
		System.out.println("Persistence done in: "+watch.getElapsedTime());	
		return Status.OK_STATUS;
	}
	
}