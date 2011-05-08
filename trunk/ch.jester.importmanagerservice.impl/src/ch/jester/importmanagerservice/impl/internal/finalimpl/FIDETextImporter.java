package ch.jester.importmanagerservice.impl.internal.finalimpl;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.jester.common.utility.StopWatch;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.IPlayerDao;
import ch.jester.importmanagerservice.impl.abstracts.FixTextTableProvider;
import ch.jester.importmanagerservice.impl.abstracts.ITableProvider;
import ch.jester.importmanagerservice.impl.abstracts.AbstractTableImporter;
import ch.jester.model.Player;
import ch.jester.model.factories.ModelFactory;

public class FIDETextImporter extends AbstractTableImporter<String, Player>{
	@Override
	protected void persist(List<Player> pDomainObjects) {
		Job[] jobs = schedule(1, pDomainObjects);
		for(Job j:jobs){
			j.schedule();
		}
		for(Job j:jobs){
			try {
				j.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void done(){
		
	}
	@Override
	protected Player createDomainObject(Properties playerProperties) {
		String name = playerProperties.getProperty("Name").trim();
		//String firstName = playerProperties.getProperty("Vorname").trim();
		Player player = ModelFactory.getInstance().createPlayer();
		if(name.indexOf(",")!=-1){
			String sfn = name.substring(name.indexOf(",")+1).trim();
			name = name.substring(0, name.indexOf(",")).trim();
			player.setFirstName(sfn);
		}
		player.setLastName(name);
		
		String fidecode = playerProperties.getProperty("CodeFIDE");
		if(fidecode.equals("")){
			fidecode="0";
		}
		player.setFideCode(Integer.parseInt(fidecode.trim()));
		String elo = playerProperties.getProperty("Elo neu");
		if(elo==null||elo.equals("")){
			elo="0";
		}
		player.setElo(Integer.parseInt(elo));
		player.setNation("Schweiz");
		return player;
	}

	@Override
	public ITableProvider<String> initialize(InputStream pInputStream) {
		FixTextTableProvider provider = new FixTextTableProvider(pInputStream);
		provider.setCell(0, 10);
		provider.setCell(10, 40);
		provider.setHeaderEntries("CodeFIDE","Name");
		return provider;
	}
	
	class PersisterJob extends Job{	
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
			IPlayerDao playerpersister = su.getExclusiveService(IPlayerDao.class);
			//Collections.sort(pList);
			playerpersister.save(pList);
			playerpersister.close();
			watch.stop();
			System.out.println("Persistence done in: "+watch.getElapsedTime());	
			return Status.OK_STATUS;
		}
		
	}
	
	public  PersisterJob[] schedule(int nrOfJobs, List<Player> pList){
		int[][] balanced = schedule(nrOfJobs, pList.size());
		PersisterJob[] job = new PersisterJob[balanced.length];
		for(int i=0;i<nrOfJobs;i++){
			List<Player> sublist = pList.subList(balanced[i][0], balanced[i][1]);
			job[i] = new PersisterJob("BalancedJob - "+(i+1), sublist);
		}
		return job;
		
	}
	
	public static int[][] schedule(int nrOfJobs, int jobsize){
		int[][] values = new int[nrOfJobs][2]; 
		int rest = jobsize % nrOfJobs; 
		int tmpjobsize = jobsize+rest;
		int jobsizePerJob = tmpjobsize/nrOfJobs;
		int start = 0;
		int end = jobsizePerJob-1;
		for(int i=0;i<nrOfJobs;i++){
			
			if(end>=jobsize){
				end = jobsize-1;
			}
			
			values[i][0]=start;
			values[i][1]=end;
			System.out.println(start+" --> "+end);
			
			start = start + jobsizePerJob;
			end = start + jobsizePerJob-1;
		}
		return values;
		
	}
	
}
