package ch.jester.system.api.pairing;

import java.util.List;

import org.eclipse.ui.forms.editor.FormEditor;

import ch.jester.common.settings.ISettingObject;
import ch.jester.model.Category;
import ch.jester.model.Pairing;
import ch.jester.model.Tournament;
import ch.jester.system.api.pairing.ui.AbstractSystemSettingsFormPage;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.exceptions.PairingNotPossibleException;
import ch.jester.system.exceptions.TournamentFinishedException;

/**
 * Interface welches von konkreten Paarungsalgorithmen
 * implementiert werden muss.
 *
 */
public interface IPairingAlgorithm {

	/**
	 * Ausführen der Paarungen (alle Kategorien)
	 * @param tournament Turnier von welchem die Paarungen gemacht werden sollen
	 * @return Liste mit den Paarungen Index 0 = Brett 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 * @throws PairingNotPossibleException Paarungsauslosung kann nicht durchgeführt werden z.B. sind weder Runden noch Spieler zugewiesen
	 * @throws TournamentFinishedException 
	 */
	public List<Pairing> executePairings(Tournament tournament) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException;
	
	/**
	 * Ausführen der Paarungen einer einzelnen Kategorie
	 * @param category
	 * @return Liste mit den Paarungen Index 0 = Brett 1
	 * @throws NotAllResultsException Es fehlen noch Resultate um die Rangliste zu erstellen
	 * @throws PairingNotPossibleException Paarungsauslosung kann nicht durchgeführt werden z.B. sind weder Runden noch Spieler zugewiesen
	 * @throws TournamentFinishedException 
	 */
	public List<Pairing> executePairings(Category category) throws NotAllResultsException, PairingNotPossibleException, TournamentFinishedException;
	
	/**
	 * Liefert die Settings-Form-Page für das entsprechende Paarungssystem 
	 * @param editor
	 * @param tournament
	 * @return
	 */
	public AbstractSystemSettingsFormPage<ISettingObject> getSettingsFormPage(FormEditor editor, Tournament tournament);
}
