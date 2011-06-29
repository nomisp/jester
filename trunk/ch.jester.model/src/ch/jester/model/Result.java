package ch.jester.model;

public enum Result {
	BLACK_WINS("0",1.0f,0.0f), 
	BLACK_WINS_F("0F",1.0f,0.0f), 
	REMIS("X",0.5f,0.5f),
	WHITE_WINS("1",0.0f,1.0f), 
	WITHE_WINS_F("1F",0.0f,1.0f);
	
	
	Result(String pShortResult, float pPointsBlack, float pPointsWhite){
		mShortResult=pShortResult;
		//mDescription=pDes;
		mPointsForBlack=pPointsBlack;
		mPointsForWhite=pPointsWhite;
	}

	public String getShortResult(){
		return mShortResult;
	}
	public float getPointsBlack(){
		return mPointsForBlack;
	}
	public float getPointsWhite(){
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
	float mPointsForBlack;
	float mPointsForWhite;
	
}
