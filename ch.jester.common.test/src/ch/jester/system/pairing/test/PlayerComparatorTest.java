package ch.jester.system.pairing.test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Title;
import ch.jester.system.swiss.simple.util.PlayerComparator;
import ch.jester.system.swiss.simple.util.RatingType;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Testklasse um den Comparator für Spieler nach den FIDE Regeln für's Schweizer System zu testen.
 * @author Peter
 *
 */
public class PlayerComparatorTest extends ActivatorProviderForTestCase {

	@Test
	public void testPoints() {
		List<PlayerCard> players = new ArrayList<PlayerCard>();
		Player p1 = new Player();
		p1.setFirstName("Abc");
		p1.setLastName("Abc");
		p1.setNationalElo(1720);
		p1.setElo(1820);
		PlayerCard pc1 = new PlayerCard();
		pc1.setPlayer(p1);
		pc1.setPoints(5.0);
		players.add(pc1);
		Player p2 = new Player();
		p2.setFirstName("Abc");
		p2.setLastName("Abc");
		p2.setNationalElo(1720);
		p2.setElo(1820);
		PlayerCard pc2 = new PlayerCard();
		pc2.setPlayer(p2);
		pc2.setPoints(6.0);
		players.add(pc2);

		assertEquals(new Double(5.0), players.get(0).getPoints());
		assertEquals(new Double(6.0), players.get(1).getPoints());

		Collections.sort(players, new PlayerComparator(RatingType.NWZ));

		assertEquals(new Double(6.0), players.get(0).getPoints());
		assertEquals(new Double(5.0), players.get(1).getPoints());
	}

	@Test
	public void testRatingNWZ() {
		List<PlayerCard> players = new ArrayList<PlayerCard>();
		Player p1 = new Player();
		p1.setFirstName("Abc");
		p1.setLastName("Abc");
		p1.setNationalElo(1720);
		p1.setElo(1820);
		PlayerCard pc1 = new PlayerCard();
		pc1.setPlayer(p1);
		pc1.setPoints(5.0);
		players.add(pc1);
		Player p2 = new Player();
		p2.setFirstName("Abc");
		p2.setLastName("Abc");
		p2.setNationalElo(1770);
		p2.setElo(1870);
		PlayerCard pc2 = new PlayerCard();
		pc2.setPlayer(p2);
		pc2.setPoints(5.0);
		players.add(pc2);

		assertEquals(new Integer(1720), players.get(0).getPlayer().getNationalElo());
		assertEquals(new Integer(1770), players.get(1).getPlayer().getNationalElo());

		Collections.sort(players, new PlayerComparator(RatingType.NWZ));

		assertEquals(new Integer(1770), players.get(0).getPlayer().getNationalElo());
		assertEquals(new Integer(1720), players.get(1).getPlayer().getNationalElo());
	}

	@Test
	public void testRatingElo() {
		List<PlayerCard> players = new ArrayList<PlayerCard>();
		Player p1 = new Player();
		p1.setFirstName("Abc");
		p1.setLastName("Abc");
		p1.setNationalElo(1720);
		p1.setElo(1820);
		PlayerCard pc1 = new PlayerCard();
		pc1.setPlayer(p1);
		pc1.setPoints(5.0);
		players.add(pc1);
		Player p2 = new Player();
		p2.setFirstName("Abc");
		p2.setLastName("Abc");
		p2.setNationalElo(1770);
		p2.setElo(1870);
		PlayerCard pc2 = new PlayerCard();
		pc2.setPlayer(p2);
		pc2.setPoints(5.0);
		players.add(pc2);

		assertEquals(new Integer(1820), players.get(0).getPlayer().getElo());
		assertEquals(new Integer(1870), players.get(1).getPlayer().getElo());

		Collections.sort(players, new PlayerComparator(RatingType.ELO));

		assertEquals(new Integer(1870), players.get(0).getPlayer().getElo());
		assertEquals(new Integer(1820), players.get(1).getPlayer().getElo());
	}

	@Test
	public void testRatingEstimatedElo() {
		List<PlayerCard> players = new ArrayList<PlayerCard>();
		Player p1 = new Player();
		p1.setFirstName("Abc");
		p1.setLastName("Abc");
		p1.setNationalElo(1720);
		p1.setElo(1820);
		p1.setEstimatedElo(1920);
		PlayerCard pc1 = new PlayerCard();
		pc1.setPlayer(p1);
		pc1.setPoints(5.0);
		players.add(pc1);
		Player p2 = new Player();
		p2.setFirstName("Abc");
		p2.setLastName("Abc");
		p2.setNationalElo(1770);
		p2.setElo(1870);
		p2.setEstimatedElo(1970);
		PlayerCard pc2 = new PlayerCard();
		pc2.setPlayer(p2);
		pc2.setPoints(5.0);
		players.add(pc2);

		assertEquals(new Integer(1920), players.get(0).getPlayer().getEstimatedElo());
		assertEquals(new Integer(1970), players.get(1).getPlayer().getEstimatedElo());

		Collections.sort(players, new PlayerComparator(RatingType.ESTIMATED));

		assertEquals(new Integer(1970), players.get(0).getPlayer().getEstimatedElo());
		assertEquals(new Integer(1920), players.get(1).getPlayer().getEstimatedElo());
	}

	@Test
	public void testTitle() {
		List<PlayerCard> players = new ArrayList<PlayerCard>();
		Player p1 = new Player();
		p1.setFirstName("Abc");
		p1.setLastName("Abc");
		p1.setNationalElo(1720);
		p1.setElo(1820);
		p1.setTitle(Title.IM);
		PlayerCard pc1 = new PlayerCard();
		pc1.setPlayer(p1);
		pc1.setPoints(5.0);
		players.add(pc1);
		Player p2 = new Player();
		p2.setFirstName("Abc");
		p2.setLastName("Abc");
		p2.setNationalElo(1720);
		p2.setElo(1820);
		p2.setTitle(Title.IGM);
		PlayerCard pc2 = new PlayerCard();
		pc2.setPlayer(p2);
		pc2.setPoints(5.0);
		players.add(pc2);

		assertEquals(Title.IM, players.get(0).getPlayer().getTitle());
		assertEquals(Title.IGM, players.get(1).getPlayer().getTitle());

		Collections.sort(players, new PlayerComparator(RatingType.NWZ));

		assertEquals(Title.IGM, players.get(0).getPlayer().getTitle());
		assertEquals(Title.IM, players.get(1).getPlayer().getTitle());
	}

	@Test
	public void testLastName() {
		List<PlayerCard> players = new ArrayList<PlayerCard>();
		Player p1 = new Player();
		p1.setFirstName("Abc");
		p1.setLastName("Abc");
		p1.setNationalElo(1720);
		p1.setElo(1820);
		p1.setTitle(Title.IGM);
		PlayerCard pc1 = new PlayerCard();
		pc1.setPlayer(p1);
		pc1.setPoints(5.0);
		players.add(pc1);
		Player p2 = new Player();
		p2.setFirstName("Abc");
		p2.setLastName("Aabc");
		p2.setNationalElo(1720);
		p2.setElo(1820);
		p2.setTitle(Title.IGM);
		PlayerCard pc2 = new PlayerCard();
		pc2.setPlayer(p2);
		pc2.setPoints(5.0);
		players.add(pc2);

		assertEquals("Abc", players.get(0).getPlayer().getLastName());
		assertEquals("Aabc", players.get(1).getPlayer().getLastName());

		Collections.sort(players, new PlayerComparator(RatingType.NWZ));

		assertEquals("Aabc", players.get(0).getPlayer().getLastName());
		assertEquals("Abc", players.get(1).getPlayer().getLastName());
	}

	@Test
	public void testFirstName() {
		List<PlayerCard> players = new ArrayList<PlayerCard>();
		Player p1 = new Player();
		p1.setFirstName("Abc");
		p1.setLastName("Abc");
		p1.setNationalElo(1720);
		p1.setElo(1820);
		p1.setTitle(Title.IGM);
		PlayerCard pc1 = new PlayerCard();
		pc1.setPlayer(p1);
		pc1.setPoints(5.0);
		players.add(pc1);
		Player p2 = new Player();
		p2.setFirstName("Aabc");
		p2.setLastName("Abc");
		p2.setNationalElo(1720);
		p2.setElo(1820);
		p2.setTitle(Title.IGM);
		PlayerCard pc2 = new PlayerCard();
		pc2.setPlayer(p2);
		pc2.setPoints(5.0);
		players.add(pc2);

		assertEquals("Abc", players.get(0).getPlayer().getFirstName());
		assertEquals("Aabc", players.get(1).getPlayer().getFirstName());

		Collections.sort(players, new PlayerComparator(RatingType.NWZ));

		assertEquals("Aabc", players.get(0).getPlayer().getFirstName());
		assertEquals("Abc", players.get(1).getPlayer().getFirstName());
	}

	@Test
	public void testAllEquals() {
		List<PlayerCard> players = new ArrayList<PlayerCard>();
		Player p1 = new Player();
		p1.setFirstName("Abc");
		p1.setLastName("Abc");
		p1.setNationalElo(1720);
		p1.setElo(1820);
		p1.setTitle(Title.IGM);
		PlayerCard pc1 = new PlayerCard();
		pc1.setPlayer(p1);
		pc1.setPoints(5.0);
		players.add(pc1);
		Player p2 = new Player();
		p2.setFirstName("Abc");
		p2.setLastName("Abc");
		p2.setNationalElo(1720);
		p2.setElo(1820);
		p2.setTitle(Title.IGM);
		PlayerCard pc2 = new PlayerCard();
		pc2.setPlayer(p2);
		pc2.setPoints(5.0);
		players.add(pc2);

		assertEquals(pc1, players.get(0));
		assertEquals(pc2, players.get(1));

		Collections.sort(players, new PlayerComparator(RatingType.ELO));

		assertEquals(pc1, players.get(0));
		assertEquals(pc2, players.get(1));
	}
}
