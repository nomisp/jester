package ch.jester.model.settings.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.jester.common.settings.ISettingObject;

public class DummySettingObject implements ISettingObject {

	private Boolean doubleRounded;
	
	private String pairingAlgorithm;
	
	private Date now;
	
	private List<String> strings;
	
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

	public List<String> getStrings() {
		return strings;
	}

	public void setStrings(List<String> strings) {
		this.strings = strings;
	}
	
}
