package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacademy.enums.Geslacht;
import be.vdab.fietsacademy.valueobjects.Adres;

public class CampusTest {
	private Docent docent1;
	private Campus campus1;
	private Campus campus2;
	@Before
	public void before() {
		campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
		campus2 = new Campus("test2", new Adres("test2", "test2", "test2", "test2"));
		docent1 = new Docent("test", "test", BigDecimal.TEN, "test@fietsacademy.be", Geslacht.MAN, campus1);
	}
	@Test
	public void campus1IsDeCampusVanDocent1() {
		assertEquals(campus1, docent1.getCampus());
		assertEquals(1, campus1.getDocenten().size());
		assertTrue(campus1.getDocenten().contains(docent1));
	}
	@Test
	public void docent1VerhuistNaarCampus2() {
		assertTrue(campus2.add(docent1));
		assertTrue(campus1.getDocenten().isEmpty());
		assertEquals(1, campus2.getDocenten().size());
		assertTrue(campus2.getDocenten().contains(docent1));
		assertEquals(campus2, docent1.getCampus());
	}
}
