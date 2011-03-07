package ch.jester.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Pairing")
public class Pairing implements Serializable {
	private static final long serialVersionUID = -5183089936204591328L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	
}
