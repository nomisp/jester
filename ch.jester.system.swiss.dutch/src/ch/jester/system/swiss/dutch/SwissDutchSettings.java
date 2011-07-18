package ch.jester.system.swiss.dutch;

import ch.jester.common.settings.ISettingObject;
import ch.jester.system.swiss.dutch.util.ByePoint;
import ch.jester.system.swiss.dutch.util.RatingType;

/**
 * Einstellungen für Paarungen nach Schweizersystem basierend auf Rating (Dutch)
 *
 */
public class SwissDutchSettings implements ISettingObject {
	
	private RatingType ratingType;
	private ByePoint byePoints = ByePoint.ONE;
	
	public SwissDutchSettings() {
		this.ratingType = RatingType.ELO;
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
}
