package ch.jester.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.AbstractModelBean;
import ch.jester.model.Category;
import ch.jester.model.Ranking;
import ch.jester.model.Tournament;

public class RankingReportInput extends AbstractModelBean<IEntityObject>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7812814460017353071L;
	Map<Category, Ranking>  mMap;
	private Tournament mTournament;
	private List<RankingReportInputEntry> mReportStructure = new ArrayList<RankingReportInputEntry>();
	public RankingReportInput(Map<Category, Ranking> map){
		mTournament = map.keySet().iterator().next().getTournament();
		mMap = map;
		createReportInput();

	}
	private void createReportInput() {
		Iterator<Category> catIt = mMap.keySet().iterator();
		while(catIt.hasNext()){
			Category cat = catIt.next();
			Ranking r = mMap.get(cat);
			mReportStructure.add(new RankingReportInputEntry(cat, r));
		}
		
	}
	public RankingReportInput(Category pCategory, Ranking pRanking){
		mTournament = pCategory.getTournament();
		mMap = new HashMap<Category, Ranking>();
		mMap.put(pCategory, pRanking);
		createReportInput();
	}
	public List<RankingReportInputEntry> getInputEntries(){
		return mReportStructure;
	}
	
	public Map<Category, Ranking> getMap(){
		return mMap;
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Tournament getTournament(){
		return mTournament;
	}

	


	
	
}
