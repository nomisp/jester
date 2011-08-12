package ch.jester.model.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.AbstractModelBean;
import ch.jester.model.Category;
import ch.jester.model.Ranking;
import ch.jester.model.Round;
import ch.jester.model.Tournament;

/**
 * HelferEntity: welche für eine einfacher ReportGenerierung benötigt wird.
 * Die Klasse stellt vorhanden Inforamtionen je nach Fall einheitlich zur Verfügung.
 *
 */
public class RankingReportInput extends AbstractModelBean<IEntityObject>{
	private static final long serialVersionUID = -7812814460017353071L;
	Map<Category, Ranking>  mMap;
	private Tournament mTournament;
	private List<RankingReportInputEntry> mReportStructure = new LinkedList<RankingReportInputEntry>();
	private Round mRound;
	public RankingReportInput(Map<Category, Ranking> map){
		mTournament = map.keySet().iterator().next().getTournament();
		mMap = map;
		createReportInput();

	}
	public RankingReportInput(Tournament t){
		mTournament = t;
		detectRanking(t);
		createReportInput();

	}
	private void detectRanking(Tournament t) {
		mMap = new LinkedHashMap<Category, Ranking>();
		if(t.getCategories().isEmpty()){return;}
		boolean finalRanking = getFinalRanking(t);
		if(finalRanking){return;}
		getIntermediateRanking(t);
	
		
	
	}
	private void getIntermediateRanking(Tournament t) {
		Round highestRound = null;
		for(Category c:t.getCategories()){
			for(Round r:c.getRounds()){
				if(highestRound==null){
					highestRound = r;
					continue;
				}
				if(highestRound.getNumber()<r.getNumber() && r.getRanking()!=null){
					highestRound = r;
				}
			}
			if(highestRound!=null&&highestRound.getRanking()!=null){
				mMap.put(c, highestRound.getRanking());
			}
		}
	}
	private boolean getFinalRanking(Tournament t) {
		boolean hasFinalRanking = false;
		for(Category c:t.getCategories()){
			if(c.getRanking()==null){continue;}
			hasFinalRanking=true;
			mMap.put(c, c.getRanking());
		}
		return hasFinalRanking;
		
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
		mMap = new LinkedHashMap<Category, Ranking>();
		mMap.put(pCategory, pRanking);
		createReportInput();
	}
	public RankingReportInput(Category pCategory, Round round, Ranking pRanking){
		mTournament = pCategory.getTournament();
		mRound = round;
		mMap = new LinkedHashMap<Category, Ranking>();
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
		return null;
	}
	
	public Tournament getTournament(){
		return mTournament;
	}

	public Round getRound(){
		return mRound;
	}


	
	
}
