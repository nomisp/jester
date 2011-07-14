package ch.jester.system.swiss.dutch;

import ch.jester.common.settings.ISettingObject;

/**
 * Einstellungen für Paarungen nach Schweizersystem basierend auf Rating (Dutch)
 * @author Peter
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

	/**
	 * Rating welches für die Paarungen verwendet wird
	 * @author Peter
	 *
	 */
	public enum RatingType {
		ELO("Elo"), NWZ("NationalElo");
		
		RatingType(String ratingType) {
			
		}
	}
	
	/**
	 * Punkte welche ein Spieler bei einem Freilos erhält.
	 * @author Peter
	 *
	 */
	public enum ByePoint {
		ZERO(0.0), HALF(0.5), ONE(1.0);
		
		private final Double points;
		
		ByePoint(Double points) {
			this.points = points;
		}
		
		public Double getPoints() {
			return points;
		}
	}
}
