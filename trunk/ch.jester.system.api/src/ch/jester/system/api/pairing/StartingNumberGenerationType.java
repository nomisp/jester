package ch.jester.system.api.pairing;

/**
 * Enum für die Einstellung wie die Startnummern vergeben werden sollen.
 * RANDOM:			Spieler erhalten zufällig ihre Startnummer
 * ELO:				Spieler werden anhand ihrer Elo/NWZ aufgestellt
 * ADDING_ORDER:	Die Spieler erhalten ihre Startnummer in der Reihenfolge wie sie zur Kategorie hinzugefügt wurden.
 * @author Peter
 *
 */
public enum StartingNumberGenerationType {
	RANDOM("RANDOM"), ELO("ELO"), ADDING_ORDER("ADDING_ORDER");
	
	StartingNumberGenerationType(String value) {
		
	}
}
