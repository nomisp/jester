package ch.jester.common.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;


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
				IPlayerDao persister = null;
						//for(int i=0;i<5;i++){
							persister = su.getDaoServiceByServiceInterface(IPlayerDao.class);
							System.out.println("Persister : "+persister);
						//}
						StopWatch watch = new StopWatch();
						watch.start();
						monitor.beginTask(this.getName()+" -- importing...", chunksize);
						List<Player> pList = new ArrayList<Player>();
						for(int i=0;i<chunksize;i++){
						
						Player player =ModelFactory.getInstance().createPlayer();
						pList.add(player);
						player.setCity("Zürich");
						player.setElo(i);
						player.setFideCode(9);
						player.setFirstName("john");
						player.setLastName("doe "+i);
						player.setNation("CH");
						//Class c = PlayerListController.class;
						
						//persister.save(player);
						//persister.save(player);
						if(i%1000==0){
							monitor.worked(1000);
							su.getService(IPlayerDao.class).save(pList);
							//persister.save(pList);
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
					StopWatch w0 = new StopWatch();
					w0.start();
					int players = su.getDaoServiceByServiceInterface(IPlayerDao.class).getAll().size();
					w0.stop();
					System.out.println("Got "+players+" with DAO select in "+w0.getElapsedTime()+" seconds");
					
				}
			//	emf.close();
				return Status.OK_STATUS;
			}
			
		};
		joinjob.setSystem(true);
		joinjob.schedule();
	}
}
