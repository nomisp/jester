package ch.jester.system.vollrundig;

import ch.jester.common.settings.ISettingObject;

/**
 * Settings zu einem Turnier mit dem Round-Robin Algorithmus
 * @author Peter
 *
 */
public class RoundRobinSettings implements ISettingObject {

	private Boolean doubleRounded;
	
	public RoundRobinSettings() {
		doubleRounded = Boolean.FALSE;	// Default keine Rückrunde
	}

	public Boolean getDoubleRounded() {
		return doubleRounded;
	}

	public void setDoubleRounded(Boolean doubleRounded) {
		this.doubleRounded = doubleRounded;
	}
	
}
