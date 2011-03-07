package ch.jester.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Player")
public class Player implements Serializable {
	private static final long serialVersionUID = -2351315088207630377L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
}
