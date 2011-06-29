package ch.jester.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Beschreibt ein Setting-Objekt
 * @author Peter
 *
 */
@Entity
@Table(name="SettingItem")
@NamedQueries({@NamedQuery(name="SettingItemByTournament", query="select s from SettingItem s where s.tournament = :tournament")})
public class SettingItem extends AbstractModelBean<SettingItem> {
	private static final long serialVersionUID = -7737771947626375572L;

    @OneToOne(optional = true, cascade=CascadeType.ALL)
	private Tournament tournament;
	
	@Column(name="RootClassName")
	private String rootClassName;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "settingItem", orphanRemoval=true)
    private Set<SettingItemValue> settingItemValues = new HashSet<SettingItemValue>();

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
		// Bidirektion
		if (tournament.getSettingItem() != this) tournament.setSettingItem(this);
	}

	public String getRootClassName() {
		return rootClassName;
	}

	public void setRootClassName(String rootClassName) {
		this.rootClassName = rootClassName;
	}

	public Set<SettingItemValue> getSettingItemValues() {
		return settingItemValues;
	}

	public void setSettingItemValues(Set<SettingItemValue> settingItemValues) {
		this.settingItemValues = settingItemValues;
	}

    public void addSettingItemValue(SettingItemValue settingItemValue) {
        if (settingItemValue == null)
            throw new IllegalArgumentException("settingItemValue Object is NULL");

        if (settingItemValue.getSettingItem() == null || !settingItemValue.getSettingItem().equals(this)) {
            settingItemValue.setSettingItem(this);
        }

        if (!this.settingItemValues.contains(settingItemValue)) {
            this.settingItemValues.add(settingItemValue);
        }
    }

    public void removeSettingItemValue(SettingItemValue settingItemValue) {
        if (settingItemValue == null) throw new IllegalArgumentException("settingItemValue Object is NULL");

        if (settingItemValue.getSettingItem() != null && settingItemValue.getSettingItem().equals(this)) {
            this.settingItemValues.remove(settingItemValue);
            settingItemValue.setSettingItem(null);
        }
    }

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
