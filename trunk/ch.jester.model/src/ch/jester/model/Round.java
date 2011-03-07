package ch.jester.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Round")
public class Round implements Serializable {
	private static final long serialVersionUID = 6672346214824111918L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="RoundNumber")
	private int number;
	
	@ManyToOne
	private Category category;
	
	

}
