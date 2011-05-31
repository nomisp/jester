package ch.jester.system.vollrundig;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.dao.ICategoryDao;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.system.api.pairing.IPairingAlgorithm;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.vollrundig.internal.VollrundigSystemActivator;

public class VollrundigPairingAlgorithm implements IPairingAlgorithm {

	private Category category;
	
	@Override
	public List<Pairing> executePairings(Tournament tournament, IProgressMonitor pMonitor) throws NotAllResultsException, PairingNotPossibleException {
		List<Pairing> allPairings = new ArrayList<Pairing>();
		for (Category category : tournament.getCategories()) {
			allPairings.addAll(executePairings(category, pMonitor));
		}
		return allPairings;
	}

	@Override
	public List<Pairing> executePairings(Category category, IProgressMonitor pMonitor) throws PairingNotPossibleException, NotAllResultsException {
		this.category = category;
		isPairingPossible();
		checkResults();
		return null;
	}
	
	/**
	 * Überprüfen ob die Anzahl Runden für die Anzahl Spieler stimmt.
	 * Gegebenenfalls fehlende Anzahl Runden anlegen
	 * @return false wenn weder Runden noch Spieler 
	 */
	private boolean isPairingPossible() throws PairingNotPossibleException {
		if (category.getPlayers().size() == 0) {
			throw new PairingNotPossibleException("No players available in Category: " + category.getDescription());
		}
		if (isNumberOfPlayersEven()) {
			createRounds((category.getPlayers().size()-1)-category.getRounds().size());
		} else {
			createRounds(category.getPlayers().size()-category.getRounds().size());
		}
		
		return true;
	}
	
	/**
	 * Überprüfen ob die Anzahl der Spieler gerade oder ungerade ist
	 * @return
	 */
	private boolean isNumberOfPlayersEven() {
		int nrPlayers = category.getPlayers().size();
		return nrPlayers % 2 == 0;
	}
	
	/**
	 * Anlegen der fehlenden Runden
	 * Bei gerader Anzahl Spieler: n-1 bei ungerader Anzahl Spieler n
	 * @param numberOfRounds Anzahl anzulegender Runden
	 * @return true wenn Runden angelegt werden mussten.
	 */
	private int createRounds(int numberOfRounds) {
		if (numberOfRounds > 0) return 0;
		int cnt = category.getRounds().size();
		ModelFactory modelFactory = ModelFactory.getInstance();
		for (int i = 0; i < numberOfRounds; i++) {
			Round round = modelFactory.createRound(++cnt);
			round.setCategory(category);
			category.addRound(round);
		}
		IDaoService<Category> catDao= VollrundigSystemActivator.getDefault().getActivationContext().getService(ICategoryDao.class);
		catDao.save(category);
		return cnt-category.getRounds().size();
	}

	/**
	 * Überprüfen, ob alle Resultate der letzten Runde erfasst wurden
	 * @throws NotAllResultsException
	 */
	private void checkResults() throws NotAllResultsException {
		List<Round> rounds = category.getRounds();
		Round lastRound = rounds.get(rounds.size()-1);
		for (Pairing pairing : lastRound.getPairings()) {
			if (pairing.getResult() == null) throw new NotAllResultsException("Not yet implemented!");
		}
	}

}
