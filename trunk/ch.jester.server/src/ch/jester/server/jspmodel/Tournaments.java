package ch.jester.server.jspmodel;

import java.util.List;

import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Tournament;

public class Tournaments {
	ServiceUtility su = new ServiceUtility();
	
	public Tournaments(){
		
	}
	
	public List<Tournament> getTournaments(){
		return su.getDaoServiceByEntity(Tournament.class).createNamedQuery("AllActiveTournaments").getResultList();
		
	}
}
