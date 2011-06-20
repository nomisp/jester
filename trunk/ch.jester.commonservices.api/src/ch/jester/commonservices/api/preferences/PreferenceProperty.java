package ch.jester.commonservices.api.preferences;

public class PreferenceProperty implements IPreferenceProperty {
	IPreferenceManager mManager;
	
	public PreferenceProperty(IPreferenceManager pManager){
		mManager=pManager;
	}
	
	@Override
	public String getInternalKey() {
		return mKey;
	}
	@Override
	public void setInternalKey(String key) {
		this.mKey = key;
	}
	@Override
	public String getLabel() {
		return mLabel;
	}
	@Override
	public void setLabel(String label) {
		this.mLabel = label;
	}
	@Override
	public Object getValue() {
		return mValue;
	}
	
	private Object convert(Object value){
		Object mValue = null;
		if(value!=null&&value.getClass() == String.class){
			if(mType == Boolean.class){
				if(value.toString().toLowerCase().equals("false")){
					mValue = false;
				}else if(value.toString().toLowerCase().equals("true")){
					mValue = true;
				}else{
					throw new RuntimeException("invalid value");
				}
			}else if(mType == Integer.class){
					if(value.toString().length()==0){
						value="0";
					}
					mValue = Integer.parseInt(value.toString());
			}else{
				mValue = value.toString();
			}
			return mValue;
		}
		return value;
	}
	
	@Override
	public void setValue(Object value) {
		Object oldValue = this.mValue;
		mValue = convert(value);

		if(oldValue==mValue){
			return;
		}
		if(oldValue!=null && oldValue.equals(mValue)){
			return;
		}
		mManager.propertyValueChanged(this);
	}
	@Override
	public Class getType() {
		return mType;
	}
	@Override
	public void setType(Class type) {
		this.mType = type;
	}
	String mKey;
	String mLabel;
	Object mValue;
	Class mType;
	private Object mDefValue;
	@Override
	public IPreferenceManager getManager() {
		return mManager;
	}

	@Override
	public String getExternalKey() {
		return mManager.getPrefixKey()+"."+mKey;
	}

	@Override
	public Object getDefaultValue() {
		return mDefValue;
	}

	@Override
	public void setDefaultValue(Object value) {
		mDefValue=convert(value);
	}
}
