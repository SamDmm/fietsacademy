package be.vdab.fietsacademy.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "groepscursussen")
public class GroepsCursus extends Cursus {
	private static final long serialVersionUID = 1L;
	private LocalDate van;
	private LocalDate tot;
	
	public GroepsCursus(String naam, LocalDate van, LocalDate tot) {
		super(naam);
		this.van = van;
		this.tot = tot;
	}
	protected GroepsCursus() {
	}
	
	public LocalDate getVan() {
		return van;
	}
	public LocalDate getTot() {
		return tot;
	}
}
