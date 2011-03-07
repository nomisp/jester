package ch.jester.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Club")
public class Club implements Serializable {
	private static final long serialVersionUID = 3749001233544554089L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
}
