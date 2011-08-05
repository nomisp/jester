package ch.jester.system.swiss.simple.util;

/**
 * Punkte welche ein Spieler bei einem Freilos erhält.
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