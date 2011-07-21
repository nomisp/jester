package ch.jester.system.swiss.dutch;

import ch.jester.common.settings.ISettingObject;
import ch.jester.system.swiss.dutch.util.ByePoint;
import ch.jester.system.swiss.dutch.util.FirstRoundColorPref;
import ch.jester.system.swiss.dutch.util.RatingType;

/**
 * Einstellungen für Paarungen nach Schweizersystem basierend auf Rating (Dutch)
 *
 */
public class SwissDutchSettings implements ISettingObject {
	
	private RatingType ratingType;
	private ByePoint byePoints;
	private FirstRoundColorPref firstRoundColor;
	
	public SwissDutchSettings() {
		this.ratingType = RatingType.ELO;
		this.byePoints = ByePoint.ONE;
		this.firstRoundColor = FirstRoundColorPref.WHITE;
	}

	public RatingType getRatingType() {
		return ratingType;
	}

	public void setRatingType(RatingType ratingType) {
		this.ratingType = ratingType;
	}

	public ByePoint getByePoints() {
		return byePoints;
	}

	public void setByePoints(ByePoint byePoints) {
		this.byePoints = byePoints;
	}

	public FirstRoundColorPref getFirstRoundColor() {
		return firstRoundColor;
	}

	public void setFirstRoundColor(FirstRoundColorPref firstRoundColor) {
		this.firstRoundColor = firstRoundColor;
	}
}
