package ch.jester.model.util;

enum ResultEntry{
	ZERO,
	ZEROF,
	X,
	ONE,
	ONEF;
};

public enum Result {

	
	
	
	
	
	
	
	WHITE_ZERO("0",0.0,1.0,3), 
	WHITE_ZERO_F("0F",0.0,1.0,4), 
	REMIS("X",0.5,0.5,2),
	WHITE_ONE("1",1.0,0.0,0), 
	WHITE_ONE_F("1F",1.0,0.0,1);
	
	

	Result(String pShortResult, double pPointsWhite, double pPointsBlack, int oppositeOrdinal){
		mShortResult=pShortResult;
		mPointsForWhite=pPointsWhite;
		mPointsForBlack=pPointsBlack;
		mOppositeOrdinal = oppositeOrdinal;
	}

	public String getShortResult(){
		return mShortResult;
	}
	public Double getPointsBlack(){
		return mPointsForBlack;
	}
	public Result getOpposite(){
		return Result.values()[mOppositeOrdinal];
	}
	public Double getPointsWhite(){
		return mPointsForWhite;
	}
	public String toString(){
		return getShortResult();
	}

	public static ResultCombination[] toResultCombinationViewForPlayer(){
		ResultCombination[] comb = new ResultCombination[Result.values().length];
		for(int i=0;i<comb.length;i++){
			comb[i] = Result.values()[i].toResultCombinationForPlayer();
		}
		return comb;
	}
	public static ResultCombination[] toResultCombinationViewForPairing(){
		ResultCombination[] comb = new ResultCombination[Result.values().length];
		for(int i=0;i<comb.length;i++){
			comb[i] = Result.values()[i].toResultCombinationForPairing();
		}
		return comb;
	}
	

	public ResultCombination toResultCombinationForPairing(){
		return resultCombination;
	}
	public ResultCombination toResultCombinationForPlayer(){
		return presultCombination;
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
	int mOppositeOrdinal;
	ResultCombination resultCombination = new ResultCombination(this);
	ResultCombination presultCombination = new PlayerResultCombination(this);
	public class ResultCombination{
		Result r;
		public ResultCombination(Result r) {
			this.r=r;
		}
		public String toString(){
			if(this.r==REMIS){
				return getShortResult();
			}
			return this.r.getShortResult()+" : "+this.r.getOpposite().getShortResult();
		}
		public Result getResult() {
			return r;
		}
	}
	class PlayerResultCombination extends ResultCombination{

		public PlayerResultCombination(Result r) {
			super(r);
		}
		public String toString(){
			return this.r.getShortResult();
		}
	}
}
