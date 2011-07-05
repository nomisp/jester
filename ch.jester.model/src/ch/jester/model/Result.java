package ch.jester.model;

public enum Result {
	BLACK_WINS("0",0.0f,1.0f), 
	BLACK_WINS_F("0F",0.0f,1.0f), 
	REMIS("X",0.5f,0.5f),
	WHITE_WINS("1",1.0f,0.0f), 
	WHITE_WINS_F("1F",1.0f,0.0f);
	
	
	Result(String pShortResult, float pPointsWhite, float pPointsBlack){
		mShortResult=pShortResult;
		mPointsForWhite=pPointsWhite;
		mPointsForBlack=pPointsBlack;
	}

	public String getShortResult(){
		return mShortResult;
	}
	public Double getPointsBlack(){
		return new Double(mPointsForBlack);
	}
	public Double getPointsWhite(){
		return new Double(mPointsForWhite);
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
	float mPointsForBlack;
	float mPointsForWhite;
	
}
