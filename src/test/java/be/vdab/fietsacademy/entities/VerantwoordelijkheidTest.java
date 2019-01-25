package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacademy.enums.Geslacht;
import be.vdab.fietsacademy.valueobjects.Adres;

public class VerantwoordelijkheidTest {
	private Verantwoordelijkheid verantwoordelijkheid1;
	private Docent docent1;
	private Campus campus1;
	@Before
	public void before() {
		verantwoordelijkheid1 = new Verantwoordelijkheid("EHBO");
		campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
		docent1 = new Docent("test", "test", BigDecimal.TEN, "test@fietsacademy.be", Geslacht.MAN, campus1);
	}
	@Test
	public void docentToevoegen() {
		assertTrue(verantwoordelijkheid1.getDocenten().isEmpty());
		assertTrue(verantwoordelijkheid1.add(docent1));
		assertEquals(1, verantwoordelijkheid1.getDocenten().size());
		assertTrue(verantwoordelijkheid1.getDocenten().contains(docent1));
		assertEquals(1, docent1.getVerantwoordelijkheden().size());
		assertTrue(docent1.getVerantwoordelijkheden().contains(verantwoordelijkheid1));
	}
	@Test
	public void docentVerwijderen() {
		assertTrue(verantwoordelijkheid1.add(docent1));
		assertTrue(verantwoordelijkheid1.remove(docent1));
		assertTrue(verantwoordelijkheid1.getDocenten().isEmpty());
		assertTrue(docent1.getVerantwoordelijkheden().isEmpty());
	}
}
