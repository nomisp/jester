package ch.jester.model.util;

import java.util.List;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.AbstractModelBean;
import ch.jester.model.Category;
import ch.jester.model.Ranking;
import ch.jester.model.RankingEntry;

public class RankingReportInputEntry extends AbstractModelBean<IEntityObject>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4133720223886956545L;
	private Category mCat;
	private Ranking mRanking;

	public RankingReportInputEntry(Category cat, Ranking ranking){
		mCat = cat;
		mRanking =ranking;
		
	}
	public Category getCategory(){
		return mCat;
	}
	public Ranking getRanking(){
		return mRanking;
	}
	public List<RankingEntry> getRankingEntries(){
		return mRanking.getRankingEntries();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
