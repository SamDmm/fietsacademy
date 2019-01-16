package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import be.vdab.fietsacademy.entities.Docent;
import be.vdab.fietsacademy.enums.Geslacht;
import be.vdab.fietsacademy.queryresults.AantalDocentenPerWedde;
import be.vdab.fietsacademy.queryresults.IdEnEmailAdres;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/insertDocent.sql")
@Import(JpaDocentRepository.class)
public class JpaDocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final String DOCENTEN = "docenten";
	private Docent docent;
	
	@Autowired
	private JpaDocentRepository repository;
	
	private long idVanTestMan() {
		return super.jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testM'", Long.class);
	}
	@Before
	public void before() {
		docent = new Docent("test", "test", BigDecimal.TEN, "test@fietsacademy.be", Geslacht.MAN);
	}
	@Test
	public void read() {
		Docent docent = repository.read(idVanTestMan()).get();
		assertEquals("testM", docent.getVoornaam());
	}
	@Test
	public void readOnberstaandeDocent() {
		assertFalse(repository.read(-1).isPresent());
	}
	private long idVanTestVrouw() {
		return super.jdbcTemplate.queryForObject("select id from docenten where voornaam='testV'", Long.class);
	}
	@Test
	public void man() {
		assertEquals(Geslacht.MAN, repository.read(idVanTestMan()).get().getGeslacht());
	}
	@Test
	public void vrouw() {
		assertEquals(Geslacht.VROUW, repository.read(idVanTestVrouw()).get().getGeslacht());
	}
	@Test
	public void create() {
		int aantalDocenten = super.countRowsInTable(DOCENTEN);
		repository.create(docent);
		assertEquals(aantalDocenten + 1, super.countRowsInTable("docenten"));
		assertNotEquals(0, docent.getId());
		assertEquals(1, super.countRowsInTableWhere(DOCENTEN, "id=" + docent.getId()));
	}
	@Autowired
	private EntityManager manager;
	@Test
	public void delete() {
		long id = idVanTestMan();
		int aantalDocenten = super.countRowsInTable(DOCENTEN);
		repository.delete(id);
		manager.flush();
		assertEquals(aantalDocenten - 1, super.countRowsInTable(DOCENTEN));
		assertEquals(0, super.countRowsInTableWhere(DOCENTEN, "id=" + id));
	}
	@Test
	public void findAll() {
		List<Docent> docenten = repository.findAll();
		assertEquals(super.countRowsInTable(DOCENTEN), docenten.size());
		BigDecimal vorigeWedde = BigDecimal.ZERO;
		for (Docent docent : docenten) {
			assertTrue(docent.getWedde().compareTo(vorigeWedde) >= 0);
			vorigeWedde = docent.getWedde();
		}
	}
	@Test
	public void findByWeddeBetween() {
		BigDecimal duizend = BigDecimal.valueOf(1_000);
		BigDecimal tweeduizend = BigDecimal.valueOf(2_000);
		List<Docent> docenten = repository.findByWeddeBetween(duizend, tweeduizend);
		long aantalDocenten = super.countRowsInTableWhere(DOCENTEN, "wedde between 1000 and 2000");
		assertEquals(aantalDocenten, docenten.size());
		docenten.forEach(docent -> {
			assertTrue(docent.getWedde().compareTo(duizend) >= 0);
			assertTrue(docent.getWedde().compareTo(tweeduizend) <= 0);
		});
	}
	@Test
	public void findEmailAdressen() {
		List<String> adressen = repository.findEmailAdressen();
		long aantal = super.jdbcTemplate.queryForObject("select count(distinct emailadres) from docenten", Long.class);
		assertEquals(aantal, adressen.size());
		adressen.forEach(adres -> assertTrue(adres.contains("@")));
	}
	@Test
	public void findIdsEnEmailAdressen() {
		List<IdEnEmailAdres> idsEnAdressen = repository.findIdsEnEmailAdressen();
		assertEquals(super.countRowsInTable(DOCENTEN), idsEnAdressen.size());
	}
	@Test
	public void findGrootsteWedde() {
		BigDecimal grootste = repository.findGrootsteWedde();
		BigDecimal grootste2 = super.jdbcTemplate.queryForObject("select max(wedde) from docenten", BigDecimal.class);
		assertEquals(0, grootste.compareTo(grootste2));
	}
	@Test
	public void findAantalDocentenPerWedde() {
		List<AantalDocentenPerWedde> aantalDocentenPerWedde = repository.findAantalDocentenPerWedde();
		long aantalUniekeWeddes = super.jdbcTemplate.queryForObject("select count(distinct wedde) from docenten", Long.class);
		assertEquals(aantalUniekeWeddes, aantalDocentenPerWedde.size());
		long aantalDocentenMetWedde1000 = super.countRowsInTableWhere(DOCENTEN, "wedde = 1000");
		aantalDocentenPerWedde.stream().filter(aantalPerWedde -> aantalPerWedde.getWedde().compareTo(BigDecimal.valueOf(1_000)) == 0).forEach(aantalPerWedde -> assertEquals(aantalDocentenMetWedde1000,  aantalPerWedde.getAantal()));
	}
	@Test
	public void algemeneOpslag() {
		int aantalAangepast = repository.algemeneOpslag(BigDecimal.TEN);
		assertEquals(super.countRowsInTable(DOCENTEN), aantalAangepast);
		BigDecimal nieuweWedde = super.jdbcTemplate.queryForObject("select wedde from docenten where id=?", BigDecimal.class, idVanTestMan());
		assertEquals(0, BigDecimal.valueOf(1_100).compareTo(nieuweWedde));
	}
}
