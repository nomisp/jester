package ch.jester.common.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.jester.common.utility.ServiceUtility;
import ch.jester.common.utility.StopWatch;
import ch.jester.dao.IPlayerPersister;
import ch.jester.model.Player;

public class DummyImportDBPerf {
	static final int targetsize = 10000;

	static final int chunksize = 10000;
	
	public static void testImportJPA() {


		
		final int jobsize = targetsize/chunksize;
		final ServiceUtility su = new ServiceUtility();
		

		IProgressMonitor groupmonitor = Job.getJobManager().createProgressGroup();
		groupmonitor.beginTask("Loading Players into DB (JPA)", targetsize);
		final Job[] importjobs = new Job[jobsize];
		final HashMap<String, StopWatch> watches = new HashMap<String, StopWatch>();
		for(int i=0;i<jobsize;i++){
		importjobs[i] = new Job("DB DummyImportJob JPA: "+i){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
						IPlayerPersister persister = su.getService(IPlayerPersister.class);
						StopWatch watch = new StopWatch();
						watch.start();
						monitor.beginTask(this.getName()+" -- importing...", chunksize);
						List<Player> pList = new ArrayList<Player>();
						for(int i=0;i<chunksize;i++){
						
						Player player = new Player();
						pList.add(player);
						player.setCity("Zürich");
						player.setElo(i);
						player.setFideCode(9);
						player.setFirstName("john");
						player.setLastName("doe");
						player.setNation("CH");
						//persister.save(player);
						//persister.save(player);
						if(i%1000==0){
							monitor.worked(1000);
							persister.save(pList);
							pList.clear();
						}
						
						
						
						}
						persister.save(pList);
						persister.close();
						monitor.done();
						watch.stop();
						watches.put(this.getName(), watch);
						return Status.OK_STATUS;
					}
				};
				
		importjobs[i].setProgressGroup(groupmonitor, chunksize);		
		importjobs[i].schedule();
		}

		Job joinjob = new Job("JoinJob"){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for(Job job:importjobs){
					try {
						job.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Iterator<String> watchKey = watches.keySet().iterator();
				while(watchKey.hasNext()){
					String key = watchKey.next();
					StopWatch watch = watches.get(key);
					System.out.println(key+" was running for "+watch.getElapsedTime()+" inserted "+chunksize+" Players");
					float avg =chunksize / watch.getElapsedTime();
					System.out.println(" avg: "+avg+" trx/sec");
					
				}
			//	emf.close();
				return Status.OK_STATUS;
			}
			
		};
		joinjob.setSystem(true);
		joinjob.schedule();
	}
/*	public static void testImportHibernate() {

		final int jobsize = targetsize/chunksize;


		IProgressMonitor groupmonitor = Job.getJobManager().createProgressGroup();
		groupmonitor.beginTask("Loading Players into DB (Hibernate)", targetsize);

		final Job[] importjobs = new Job[jobsize];
		final HashMap<String, StopWatch> watches = new HashMap<String, StopWatch>();
		for(int i=0;i<jobsize;i++){
		importjobs[i] = new Job("DB DummyImportJob Hibernate: "+i){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
						final Session session = HibernatehelperPlugin.getSession();
						StopWatch watch = new StopWatch();						
						watch.start();
						
						Transaction trx = session.beginTransaction();
						trx.begin();
						monitor.beginTask(this.getName()+" -- importing...", chunksize);
						for(int i=0;i<chunksize;i++){
						
						Player player = new Player();
						player.setCity("Zürich");
						player.setElo(i);
						player.setFideCode(9);
						player.setFirstName("john");
						player.setLastName("doe");
						player.setNation("CH");
						session.persist(player);
						
						if(i%1000==0){
							monitor.worked(1000);
							session.flush();
						}
						
						
						
						}
						trx.commit();
						
						monitor.done();
						watch.stop();
						watches.put(this.getName(), watch);
						
						session.close();
						return Status.OK_STATUS;
					}
				};
				
		importjobs[i].setProgressGroup(groupmonitor, chunksize);		
		importjobs[i].schedule();
		}

		Job joinjob = new Job("JoinJob"){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for(Job job:importjobs){
					try {
						job.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Iterator<String> watchKey = watches.keySet().iterator();
				while(watchKey.hasNext()){
					String key = watchKey.next();
					StopWatch watch = watches.get(key);
					System.out.println(key+" was running: "+watch.getElapsedTime()+" inserted "+chunksize+" Players");
					float avg =chunksize / watch.getElapsedTime();
					System.out.println(" avg: "+avg+" trx/sec");
					
				}
				return Status.OK_STATUS;
			}
			
		};
		joinjob.setSystem(true);
		joinjob.schedule();
	}*/
}
