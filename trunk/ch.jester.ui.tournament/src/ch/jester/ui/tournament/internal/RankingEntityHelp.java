package ch.jester.ui.tournament.internal;

import java.util.List;
import java.util.Map;

import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.model.AbstractModelBean;
import ch.jester.model.Category;
import ch.jester.model.Player;
import ch.jester.model.Ranking;

public class RankingEntityHelp extends AbstractModelBean<IEntityObject>{
	Map<Category, Ranking>  mMap;
	private List<Player> list;
	public RankingEntityHelp(List<Player> pList){
		list = pList;
	}
	public RankingEntityHelp(Map<Category, Ranking> map){
		mMap = map;
	}
	public Map<Category, Ranking> getRankingInputMap(){
		return mMap;
	}
	public List<Player> getPlayerInputListForTest(){
		return list;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
