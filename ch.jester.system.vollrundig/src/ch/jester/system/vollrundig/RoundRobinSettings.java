package ch.jester.system.vollrundig;

import ch.jester.common.settings.ISettingObject;
import ch.jester.system.api.pairing.StartingNumberGenerationType;

/**
 * Settings zu einem Turnier mit dem Round-Robin Algorithmus
 * @author Peter
 *
 */
public class RoundRobinSettings implements ISettingObject {

	private Boolean doubleRounded;
	private StartingNumberGenerationType startingNumberGenerationType;
	
	public RoundRobinSettings() {
		doubleRounded = Boolean.FALSE;	// Default keine Rückrunde
		startingNumberGenerationType = StartingNumberGenerationType.RANDOM;
	}

	public Boolean getDoubleRounded() {
		return doubleRounded;
	}

	public void setDoubleRounded(Boolean doubleRounded) {
		this.doubleRounded = doubleRounded;
	}

	public StartingNumberGenerationType getStartingNumberGenerationType() {
		return startingNumberGenerationType;
	}

	public void setStartingNumberGenerationType(StartingNumberGenerationType startingNumberGenerationType) {
		this.startingNumberGenerationType = startingNumberGenerationType;
	}
}
