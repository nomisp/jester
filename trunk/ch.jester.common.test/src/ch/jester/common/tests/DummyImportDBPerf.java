package ch.jester.common.tests;

import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;


import ch.jester.common.utility.StopWatch;
import ch.jester.model.Player;
import ch.jester.orm.ORMPlugin;

public class DummyImportDBPerf {
	static final int targetsize = 10000;

	static final int chunksize = 5000;
	
	public static void testImportJPA() {


		
		final int jobsize = targetsize/chunksize;


		IProgressMonitor groupmonitor = Job.getJobManager().createProgressGroup();
		groupmonitor.beginTask("Loading Players into DB (JPA)", targetsize);
		final EntityManagerFactory emf = ORMPlugin.getJPAEntitManagerFactor();
		final Job[] importjobs = new Job[jobsize];
		final HashMap<String, StopWatch> watches = new HashMap<String, StopWatch>();
		for(int i=0;i<jobsize;i++){
		importjobs[i] = new Job("DB DummyImportJob JPA: "+i){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
						EntityManager em = emf.createEntityManager();
			
						StopWatch watch = new StopWatch();
						watch.start();
						em.getTransaction().begin();
						monitor.beginTask(this.getName()+" -- importing...", chunksize);
						for(int i=0;i<chunksize;i++){
					//	
						
						Player player = new Player();
						player.setCity("Zürich");
						player.setElo(i);
						player.setFideCode(9);
						player.setFirstName("john");
						player.setLastName("doe");
						player.setNation("CH");
						em.persist(player);
						//em.getTransaction().commit();
						//playerBalancer.add(player);
						if(i%1000==0){
							monitor.worked(1000);
							em.flush();
							em.clear();
						}
						
						
						
						}
						em.getTransaction().commit();
						
						monitor.done();
						watch.stop();
						watches.put(this.getName(), watch);
						em.close();
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
				emf.close();
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
