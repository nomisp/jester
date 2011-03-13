package ch.jester.common.utility;

public class StopWatch {
	private long mStart;
	private long mStop;
	private boolean mRun;
	public void start(){
		mRun = true;
		mStart = System.currentTimeMillis();
	}
	public void stop(){
		mRun = false;
		mStop = System.currentTimeMillis();
	}
	public float getElapsedTime(){
		if(mRun){
			stop();
		}
		return (mStop-mStart)/1000f;
	}
	
}
