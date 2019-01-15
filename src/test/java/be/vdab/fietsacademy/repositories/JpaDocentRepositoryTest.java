package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;

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
}
