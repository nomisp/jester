package ch.jester.server.jspmodel;

import java.util.List;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Tournament;

public class Tournaments {
	ServiceUtility su = new ServiceUtility();
	
	public Tournaments(){
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Tournament> getTournaments(){
		return su.getDaoServiceByEntity(Tournament.class).createNamedQuery("AllActiveTournaments").getResultList();
		
	}
	
	public String getAllPlayersLink(Tournament t){
		return linkTournament(t, "Spielerliste", "playerlist");
	}
	
	public String getCategoryLinks(Category cat){
		return linkCat(cat, "Paarungsliste", "pairinglist_category");
	}
	
	private String linkCat(Category cat, String pLabel, String report){
		String link = "<a href=../reports?category="+cat.getId()+"&reportalias="+report+">"+pLabel+"</a>";
		return link;
	}
	
	private String linkTournament(Tournament t, String pLabel, String report){
		String link = "<a href=../reports?tournament="+t.getId()+"&reportalias="+report+">"+pLabel+"</a>";
		return link;
	}
}
