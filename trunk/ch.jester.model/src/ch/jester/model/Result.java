package ch.jester.model;

public enum Result {
	BLACK_WINS("0",0.0,1.0), 
	BLACK_WINS_F("0F",0.0,1.0), 
	REMIS("X",0.5,0.5),
	WHITE_WINS("1",1.0,0.0), 
	WHITE_WINS_F("1F",1.0,0.0);
	
	
	Result(String pShortResult, double pPointsWhite, double pPointsBlack){
		mShortResult=pShortResult;
		mPointsForWhite=pPointsWhite;
		mPointsForBlack=pPointsBlack;
	}

	public String getShortResult(){
		return mShortResult;
	}
	public Double getPointsBlack(){
		return mPointsForBlack;
	}
	public Double getPointsWhite(){
		return mPointsForWhite;
	}
	public String toString(){
		return getShortResult();
	}
	public static Result findByShortResult(String p){
		for(Result r:Result.values()){
			if(r.getShortResult().equals(p)){
				return r;
			}
		}
		
		return null;
	}
	
	String mShortResult;
	String mDescription;
	double mPointsForBlack;
	double mPointsForWhite;
	
}
