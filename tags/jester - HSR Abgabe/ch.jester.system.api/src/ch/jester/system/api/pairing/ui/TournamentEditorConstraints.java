package ch.jester.system.api.pairing.ui;

/**
 * Konfigurationsklasse für den TurnierEditor.
 * Defaultwert ist immer 'true'.
 * Die Attribute bestimmen, was der User manuell im UI machen kann/darf.
 *
 */
public class TournamentEditorConstraints {
	public final static TournamentEditorConstraints defaultContstraints = new TournamentEditorConstraints();
	/**
	 * Konfiguration ob Runden hinzugefügt werden können
	 */
	public boolean canAddRounds = true;
	/**
	 * Konfiguration ob Runden gelöscht werden können
	 */
	public boolean canRemoveRounds = true;
	/**
	 * Konfiguration ob Kategorien hinzugefügt werden können
	 */
	public boolean canAddCategories = true;
	/**
	 * Konfiguration ob Kategorien gelöscht werden können
	 */
	public boolean canRemoveCategories = true;
}
