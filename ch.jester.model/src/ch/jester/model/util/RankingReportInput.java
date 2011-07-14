package ch.jester.model.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.AbstractModelBean;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.Ranking;
import ch.jester.model.Tournament;

public class RankingReportInput extends AbstractModelBean<IEntityObject> implements Iterator<Category>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7812814460017353071L;
	Map<Category, Ranking>  mMap;
	private List<Player> list;
	private Iterator<Category> mCatIterator;
	private Tournament mTournament;
	public RankingReportInput(List<Player> pList){
		list = pList;
	}
	public RankingReportInput(Map<Category, Ranking> map){
		mTournament = map.keySet().iterator().next().getTournament();
		mMap = map;
		mCatIterator = mMap.keySet().iterator();

	}
	public RankingReportInput(Category pCategory, Ranking pRanking){
		mTournament = pCategory.getTournament();
		mMap = new HashMap<Category, Ranking>();
		mMap.put(pCategory, pRanking);
		
		mCatIterator=mMap.keySet().iterator();
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

	public Ranking getRanking(Category pCat){
		return mMap.get(pCat);
	}
	@Override
	public boolean hasNext() {
		return mCatIterator.hasNext();
	}
	@Override
	public Category next() {
		return mCatIterator.next();
	}
	@Override
	public void remove() {
		
	}
	
}
