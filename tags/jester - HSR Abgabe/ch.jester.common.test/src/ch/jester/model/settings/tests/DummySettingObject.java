package ch.jester.model.settings.tests;

import java.util.Date;

import ch.jester.common.settings.ISettingObject;

public class DummySettingObject implements ISettingObject {

	private Boolean doubleRounded;
	
	private String pairingAlgorithm;
	
	private Date now;
	
	private Integer numberOfRounds;
	
	private Double result;
	
//	private ArrayList<String> myStringList;
	
	public DummySettingObject() {
	}

	public Boolean getDoubleRounded() {
		return doubleRounded;
	}

	public void setDoubleRounded(Boolean doubleRounded) {
		this.doubleRounded = doubleRounded;
	}

	public String getPairingAlgorithm() {
		return pairingAlgorithm;
	}

	public void setPairingAlgorithm(String pairingAlgorithm) {
		this.pairingAlgorithm = pairingAlgorithm;
	}

	public Date getNow() {
		return now;
	}

	public void setNow(Date now) {
		this.now = now;
	}
//
//	public ArrayList<String> getMyStringList() {
//		return myStringList;
//	}
//
//	public void setMyStringList(ArrayList<String> myStringList) {
//		this.myStringList = myStringList;
//	}

	public Integer getNumberOfRounds() {
		return numberOfRounds;
	}

	public void setNumberOfRounds(Integer numberOfRounds) {
		this.numberOfRounds = numberOfRounds;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}
	
}
