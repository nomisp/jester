package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Beschreibt ein Property eines SettingItems
 * @author Peter
 *
 */
@Entity
@Table(name="SettingItemValue")
public class SettingItemValue extends AbstractModelBean<SettingItemValue> {
	private static final long serialVersionUID = -6921689384337411498L;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "SettingItemId")
    private SettingItem settingItem;
	
    @Column(name = "FieldNameInRootClass")
    private String fieldNameInRootClass;

    @Column(name = "FieldClassNameInRootClass")
    private String fieldClassNameInRootClass;

    @Column(name = "ValueClassName")
    private String valueClassName;

    @Column(name = "ValueFieldName")
    private String valueFieldName;

    @Column(name = "ValueAsString")
    private String valueAsString;

    @Column(name = "SequenceNo")
    private Integer sequenceNo = 0;
    
    // ******************************
    // Bidirektionale Assoziazionen
    // ******************************
	public SettingItem getSettingItem() {
		return settingItem;
	}

	public void setSettingItem(SettingItem settingItem) {
		if (this.settingItem != null && this.settingItem.getSettingItemValues().contains(this)) {
            this.settingItem.getSettingItemValues().remove(this);
        }

        this.settingItem = settingItem;

        if (settingItem != null && !settingItem.getSettingItemValues().contains(this)) {
        	settingItem.getSettingItemValues().add(this);
        }
	}
	
	// *****************************
	// Getter und Setter
	// *****************************
	public String getFieldNameInRootClass() {
		return fieldNameInRootClass;
	}

	public void setFieldNameInRootClass(String fieldNameInRootClass) {
		this.fieldNameInRootClass = fieldNameInRootClass;
	}

	public String getFieldClassNameInRootClass() {
		return fieldClassNameInRootClass;
	}

	public void setFieldClassNameInRootClass(String fieldClassNameInRootClass) {
		this.fieldClassNameInRootClass = fieldClassNameInRootClass;
	}

	public String getValueClassName() {
		return valueClassName;
	}

	public void setValueClassName(String valueClassName) {
		this.valueClassName = valueClassName;
	}

	public String getValueFieldName() {
		return valueFieldName;
	}

	public void setValueFieldName(String valueFieldName) {
		this.valueFieldName = valueFieldName;
	}

	public String getValueAsString() {
		return valueAsString;
	}

	public void setValueAsString(String valueAsString) {
		this.valueAsString = valueAsString;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
