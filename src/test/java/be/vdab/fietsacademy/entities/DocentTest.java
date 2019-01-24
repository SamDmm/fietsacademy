package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacademy.enums.Geslacht;
import be.vdab.fietsacademy.valueobjects.Adres;

public class DocentTest {
	private final static BigDecimal ORIGINELE_WEDDE = BigDecimal.valueOf(200);
	private Docent docent1;
	private Campus campus1;
	private Docent nogEensDocent1;
	private Docent docent2;
	private Campus campus2;
	@Before
	public void before() {
		campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
		docent1 = new Docent("test", "test", ORIGINELE_WEDDE, "test@fietsacademy.be", Geslacht.MAN, campus1);
		nogEensDocent1 = new Docent("test", "test", ORIGINELE_WEDDE, "test@fietsacademy.be", Geslacht.MAN, campus1);
		docent2 = new Docent("test2", "test2", ORIGINELE_WEDDE, "test2@fietsacademy.be", Geslacht.MAN, campus1);
		campus2 = new Campus("test2", new Adres("test2", "test2", "test2", "test2"));
	}
	@Test
	public void opslag() {
		docent1.opslag(BigDecimal.TEN);
		assertEquals(0, BigDecimal.valueOf(220).compareTo(docent1.getWedde()));
	}
	@Test(expected = NullPointerException.class)
	public void opslagMetNulKanNiet() {
		docent1.opslag(null);
	}
	@Test(expected = IllegalArgumentException.class)
	public void opslagMet0KanNiet() {
		docent1.opslag(BigDecimal.ZERO);
		assertEquals(0, ORIGINELE_WEDDE.compareTo(docent1.getWedde()));
	}
	@Test(expected = IllegalArgumentException.class)
	public void negatieveOpslagKanNiet() {
		docent1.opslag(BigDecimal.valueOf(-1));
		assertEquals(0, ORIGINELE_WEDDE.compareTo(docent1.getWedde()));
	}
	@Test
	public void eenNieuweDocentHeeftGeenBijnamen() {
		assertTrue(docent1.getBijnamen().isEmpty());
	}
	@Test
	public void bijnaamToevoegen() {
		assertTrue(docent1.addBijnaam("test"));
		assertEquals(1, docent1.getBijnamen().size());
		assertTrue(docent1.getBijnamen().contains("test"));
	}
	@Test
	public void tweeKeerDezelfdeBijnaamToevoegenKanNiet() {
		docent1.addBijnaam("test");
		assertFalse(docent1.addBijnaam("test"));
		assertEquals(1, docent1.getBijnamen().size());
	}
	@Test (expected = NullPointerException.class)
	public void nullAlsBijnaamToevoegenKanNiet() {
		docent1.addBijnaam(null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void eenLegeBijnaamToevoegenKanNiet() {
		docent1.addBijnaam("");
	}
	@Test (expected = IllegalArgumentException.class)
	public void eenBijnaamMetEnkelSpatiesToevoegenKanNiet() {
		docent1.addBijnaam("  ");
	}
	@Test
	public void bijnaamVerwijderen() {
		docent1.addBijnaam("test");
		assertTrue(docent1.removeBijnaam("test"));
		assertTrue(docent1.getBijnamen().isEmpty());
	}
	@Test
	public void eenBijnaamVerwijderenDieJeNietToevoegdeKanNiet() {
		docent1.addBijnaam("test");
		assertFalse(docent1.removeBijnaam("test2"));
		assertEquals(1, docent1.getBijnamen().size());
		assertTrue(docent1.getBijnamen().contains("test"));
	}
	@Test
	public void meerdereDocentenKunnenTotDezelfdeCampusBehoren() {
		assertTrue(campus1.getDocenten().contains(docent1));
		assertTrue(campus1.getDocenten().contains(docent2));
	}
	@Test
	public void docentenZijnGelijkAlsHunEmailAdressenGelijkZijn() {
		assertEquals(docent1, nogEensDocent1);
	}
	@Test
	public void docentenZijnVerschillendAlsHunEmailAdressenVerschillen() {
		assertNotEquals(docent1, docent2);
	}
	@Test
	public void eenDocentVerschiltVanNull() {
		assertNotEquals(docent1, null);
	}
	@Test
	public void eenDocentVerschiltVanEenAnderTypeObject() {
		assertNotEquals(docent1, "");
	}
	@Test
	public void gelijkeDocentenGevenDezelfdeHashCode() {
		assertEquals(docent1.hashCode(), nogEensDocent1.hashCode());
	}
	@Test
	public void docent1KomtVoorInCampus1() {
		assertEquals(docent1.getCampus(), campus1);
		assertEquals(2, campus1.getDocenten().size());
		assertTrue(campus1.getDocenten().contains(docent1));
	}
	@Test
	public void docent1VerhuistNaarCampus2() {
		docent1.setCampus(campus2);
		assertEquals(docent1.getCampus(), campus2);
		assertEquals(1, campus1.getDocenten().size());
		assertEquals(1, campus2.getDocenten().size());
		assertTrue(campus2.getDocenten().contains(docent1));
	}
}
