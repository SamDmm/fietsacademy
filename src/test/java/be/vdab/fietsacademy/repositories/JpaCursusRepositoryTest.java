package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

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

import be.vdab.fietsacademy.entities.Cursus;
import be.vdab.fietsacademy.entities.GroepsCursus;
import be.vdab.fietsacademy.entities.IndividueleCursus;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(JpaCursusRepository.class)
@Sql("/insertCursus.sql")
public class JpaCursusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final String CURSUSSEN = "cursussen";
	private static final LocalDate EEN_DATUM = LocalDate.of(2019, 1, 1);
	@Autowired
	private JpaCursusRepository repository;
	private long idVanTestGroepsCursus() {
		return super.jdbcTemplate.queryForObject("select id from cursussen where naam = 'testGroep'", Long.class);
	}
	private long idVanTestIndividueleCursus() {
		return super.jdbcTemplate.queryForObject("select id from cursussen where naam = 'testIndividueel'", Long.class);
	}
	@Test
	public void readGroepCursus() {
		Optional<Cursus> optionalCursus = repository.read(idVanTestGroepsCursus());
		assertEquals("testGroep", ((GroepsCursus) optionalCursus.get()).getNaam());
	}
	@Test
	public void readIndividueleCursus() {
		Optional<Cursus> optionalCursus = repository.read(idVanTestIndividueleCursus());
		assertEquals("testIndividueel", ((IndividueleCursus) optionalCursus.get()).getNaam());
	}
	@Test
	public void createGroepsCursus() {
		int aantalGroepsCursussen = super.countRowsInTableWhere(CURSUSSEN, "soort='G'");
		GroepsCursus cursus = new GroepsCursus("testGroep2", EEN_DATUM, EEN_DATUM);
		repository.create(cursus);
		assertEquals(aantalGroepsCursussen + 1, super.countRowsInTableWhere(CURSUSSEN, "soort='G'"));
		assertEquals(1, super.countRowsInTableWhere(CURSUSSEN, "id=" + cursus.getId()));
	}
	@Test
	public void createIndividueleCursus() {
		int aantalIndividueleCursusser = super.countRowsInTableWhere(CURSUSSEN, "soort='I'");
		IndividueleCursus cursus = new IndividueleCursus("testIndividueel2", 7);
		repository.create(cursus);
		assertEquals(aantalIndividueleCursusser + 1, super.countRowsInTableWhere(CURSUSSEN, "soort='I'"));
		assertEquals(1, super.countRowsInTableWhere(CURSUSSEN, "id=" + cursus.getId()));
	}
}
