package ch.jester.model.factories;

import java.util.Set;

import ch.jester.model.Category;
import ch.jester.model.Club;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.Round;
import ch.jester.model.Tournament;

/**
 * ModelFactory zum erzeugen von Model-Objekten,
 * welche sp�ter persistiert werden k�nnen.
 * @author Peter
 *
 */
public class ModelFactory {

	private static ModelFactory instance = new ModelFactory();
	
	private ModelFactory() {
		
	}
	
	public static ModelFactory getInstance() {
		return instance;
	}
	
	/**
	 * Erzeugt ein Turnier anhand des Namens
	 * @param name
	 * @return Turnier
	 */
	public Tournament createTournament(String name) {
		Tournament tournament = new Tournament();
		tournament.setName(name);
		return tournament;
	}
	
	/**
	 * Erzeugt ein Turnier anhand des Namens mit Kategorien
	 * @param name
	 * @param categories
	 * @return Turnier
	 */
	public Tournament createTournament(String name, Set<Category> categories) {
		Tournament tournament = createTournament(name);
		tournament.setCategories(categories);
		return tournament;
	}
	
	/**
	 * Erzeugt eine Kategorie anhand der Beschreibung
	 * @param description
	 * @return Kategorie
	 */
	public Category createCategory(String description) {
		Category cat = new Category();
		cat.setDescription(description);
		return cat;
	}
	
	/**
	 * Erzeugt einen Spieler anhand seines Namens
	 * @param firstName
	 * @param lastName
	 * @return Spieler
	 */
	public Player createPlayer(String firstName, String lastName) {
		Player player = new Player();
		player.setFirstName(firstName);
		player.setLastName(lastName);
		return player;
	}
	
	/**
	 * Erzeugen eines Spielers
	 * @param pFirstName
	 * @param pLastName
	 * @param pCity
	 * @param pElo
	 * @param pNatElo
	 * @param pFideCode
	 * @param pNation
	 * @param pNationalCode
	 * @return
	 */
	public Player createPlayer(String pFirstName, String pLastName, String pCity, 
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
	
	/**Erzeugt einen Defaultplayer mit den Werten<br>
	 * Vorname: FirstName<br>
	 * Nachname: LastName<br>
	 * Stadt: City<br>
	 * Elo/NationalElo/FideCode/NationCode: 0<br>
	 * Nation: Nation<br>
	 * @return den erzeugten Player
	 */
	public Player createPlayer(){
		return createPlayer("FirstName","LastName","City",0,0,0,"Nation",0);
	}
	
	/**
	 * Erzeugt eine Paarung anhand der beiden Spieler
	 * @param white Spieler weiss
	 * @param black Spieler schwarz
	 * @return Paarung
	 */
	public Pairing createPairing(Player white, Player black) {
		Pairing pairing = new Pairing();
		pairing.setWhite(white);
		pairing.setBlack(black);
		return pairing;
	}
	
	/**
	 * Erzeugt eine Paarung anhand der beiden Spieler
	 * @param white	Spieler weiss
	 * @param black	Spieler schwarz
	 * @param round Runde
	 * @return Paarung
	 */
	public Pairing createPairing(Player white, Player black, Round round) {
		Pairing pairing = createPairing(white, black);
		pairing.setRound(round);
		return pairing;
	}
	
	/**
	 * Erzeugt standardm�ssig die erste Runde
	 * @return Runde 1
	 */
	public Round creataRound() {
		return createRound(1);
	}
	
	/**
	 * Erzeugt eine Runde mit der Nummer der Runde
	 * @param number
	 * @return Runde #number
	 */
	public Round createRound(int number) {
		Round round = new Round();
		round.setNumber(number);
		return round;
	}
	
	/**
	 * Erzeugen eines Vereins anhand seines Namens
	 * @param name
	 * @return Verein
	 */
	public Club createClub(String name) {
		Club club = new Club();
		club.setName(name);
		return club;
	}
}