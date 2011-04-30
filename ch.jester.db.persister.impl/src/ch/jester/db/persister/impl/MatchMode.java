package ch.jester.db.persister.impl;

/**
 * Enum um die verschiedene Modes von like Suchen zu unterscheiden.
 * Exact:	Suchparameter --> Suchparameter
 * Start:	Suchparameter --> %Suchparameter
 * End:		Suchparameter --> Suchparameter%
 * Anywhere:Suchparameter --> %Suchparameter%
 * @author Peter
 *
 */
public enum MatchMode {
	EXACT, START, END, ANYWHERE
}
