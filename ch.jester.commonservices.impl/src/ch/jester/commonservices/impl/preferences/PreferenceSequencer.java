package ch.jester.commonservices.impl.preferences;


import java.util.HashSet;

import ch.jester.commonservices.api.preferences.IPreferenceProperty;
import ch.jester.commonservices.api.preferences.IPreferenceSequencer;

public class PreferenceSequencer implements IPreferenceSequencer{

	private PreferenceManager mManager;
	private String mSequenceId;
	private IPreferenceProperty mSequenceProperty;
	private IPreferenceProperty mAttributeProperty;
	private HashSet<String> mAttributes =new HashSet<String>();
	PreferenceSequencer(String pSequenceId, PreferenceManager pManager) {
		mManager = pManager;
		mSequenceId = pSequenceId;
		mSequenceProperty = mManager.create(mSequenceId+".ps.count", null, 0);
		mAttributeProperty = mManager.create(mSequenceId+".ps.attributes", null, mAttributes);
		mAttributes = (HashSet<String>) mAttributeProperty.getValue();
	}
	
	@Override
	public IPreferenceProperty create(String pKey, String pLabel, Object value) {
		IPreferenceProperty prop = mManager.create(mSequenceId+"/"+mSequenceProperty.getIntegerValue(), pKey, pLabel, value);
		mAttributes.add(pKey);
		mAttributeProperty.setValue(mAttributes);
		return prop;
	}

	@Override
	public int size() {
		return mSequenceProperty.getIntegerValue();
	}

	@Override
	public void writeNext() {
		mSequenceProperty.setValue(mSequenceProperty.getIntegerValue().intValue()+1);
	}
	

	
}
