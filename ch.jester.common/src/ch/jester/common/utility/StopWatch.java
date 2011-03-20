package ch.jester.common.utility;

/**
 * Utility Klasse um die Zeit zu messen
 */
public class StopWatch {
	private long mStart;
	private long mStop;
	private boolean mRun;
	/**
	 * Speichert die Startzeit
	 */
	public void start(){
		mRun = true;
		mStart = System.currentTimeMillis();
	}
	/**
	 * Speichert die Endzeit
	 */
	public void stop(){
		mRun = false;
		mStop = System.currentTimeMillis();
	}
	/**Gibt die Endzeit mit Startzeit in Sekunden zurück
	 * @return
	 */
	public float getElapsedTime(){
		if(mRun){
			stop();
		}
		return (mStop-mStart)/1000f;
	}
	
}
