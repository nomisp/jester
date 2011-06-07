package ch.jester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SettingItemValue")
public class SettingItemValue extends AbstractModelBean<SettingItemValue> {
	private static final long serialVersionUID = -6921689384337411498L;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "SettingItemId")
//    @org.hibernate.annotations.ForeignKey(name = "FK_SettingItemParam_SettingItem_Id", inverseName = "FK_SettingItem_SettingItemParam_Id")
//    @org.hibernate.validator.NotNull
    private SettingItem settingItem;
	
    @Column(name = "FieldNameInRootClass")
//    @org.hibernate.validator.NotNull
//    @org.hibernate.validator.Length(max = 500)
    private String fieldNameInRootClass;

    @Column(name = "FieldClassNameInRootClass")
//    @org.hibernate.validator.NotNull
//    @org.hibernate.validator.Length(max = 500)
    private String fieldClassNameInRootClass;

    @Column(name = "EntityClassName")
//    @org.hibernate.validator.Length(max = 500)
    private String entityClassName;

    @Column(name = "ValueClassName")
//    @org.hibernate.validator.NotNull
//    @org.hibernate.validator.Length(max = 500)
    private String valueClassName;

    @Column(name = "ValueFieldName")
//    @org.hibernate.validator.Length(max = 50)
    private String valueFieldName;

    @Column(name = "ValueAsString")
//    @org.hibernate.validator.NotNull
//    @org.hibernate.validator.Length(max = 1000)
    private String valueAsString;

    @Column(name = "SequenceNo")
//    @org.hibernate.validator.NotNull
//    @org.hibernate.validator.Range(min = 0, max = 99999)
    private Double sequenceNo = 0d;
    
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

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
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

	public Double getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Double sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
