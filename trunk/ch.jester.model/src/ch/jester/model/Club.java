package ch.jester.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Club")
public class Club implements Serializable {
	private static final long serialVersionUID = 3749001233544554089L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="Name", nullable=false, unique=true, length=50)
	//@org.hibernate.validator.constraints.Length(min=2, max=50)
	@NotNull
	private String name;
	
	@Column(name="Code", nullable=true)
	private int code;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
