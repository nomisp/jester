package ch.jester.importmanagerservice.impl.abstracts;

import java.util.List;

import ch.jester.model.Player;

public class JobUtil {
	public static  PersisterJob[] paralleliseJob(int nrOfJobs, List<Player> pList){
		int[][] balanced = split(nrOfJobs, pList.size());
		PersisterJob[] job = new PersisterJob[balanced.length];
		for(int i=0;i<nrOfJobs;i++){
			List<Player> sublist = pList.subList(balanced[i][0], balanced[i][1]);
			job[i] = new PersisterJob("ParallelisedJob - "+(i+1), sublist);
		}
		return job;
		
	}
	
	private static int[][] split(int nrOfJobs, int jobsize){
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
