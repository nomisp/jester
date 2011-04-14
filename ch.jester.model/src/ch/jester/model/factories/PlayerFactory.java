package ch.jester.model.factories;

import ch.jester.model.Player;

public class PlayerFactory {
	public static Player createPlayer(String pFirstName, String pLastName, String pCity, 
			int pElo, int pNatElo, int pFideCode, String pNation, int pNationalCode){
		Player player = new Player();
		player.setFirstName(pFirstName);
		player.setLastName(pLastName);
		player.setCity(pCity);
		player.setElo(pElo);
		player.setNationalElo(pNatElo);
		player.setFideCode(pFideCode);
		player.setNation(pNation);
		player.setNationalCode(pNationalCode);
		return player;
	}
	
	public static Player createPlayer(){
		return createPlayer("FirstName","LastName","City",0,0,0,"Nation",0);
	}
}