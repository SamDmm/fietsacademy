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
	private static final String GROEPS_CURSUSSEN = "groepscursussen";
	private static final String INDIVIDUELE_CURSUSSEN = "individuelecursussen";
	
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
		int aantalRecordsInCursussen = super.countRowsInTable(CURSUSSEN);
		int aantalRecordsInGroepsCursussen = super.countRowsInTable(GROEPS_CURSUSSEN);
		GroepsCursus cursus = new GroepsCursus("testGroep2", EEN_DATUM, EEN_DATUM);
		repository.create(cursus);
		assertEquals(aantalRecordsInCursussen + 1, super.countRowsInTable(CURSUSSEN));
		assertEquals(aantalRecordsInGroepsCursussen + 1, super.countRowsInTable(GROEPS_CURSUSSEN));
		assertEquals(1, super.countRowsInTableWhere(CURSUSSEN, "id=" + cursus.getId()));
		assertEquals(1, super.countRowsInTableWhere(GROEPS_CURSUSSEN, "id=" + cursus.getId()));
	}
	@Test
	public void createIndividueleCursus() {
		int aantalRecordsInCursussen = super.countRowsInTable(CURSUSSEN);
		int aantalRecordsInIndividueleCursussen = super.countRowsInTable(INDIVIDUELE_CURSUSSEN);
		IndividueleCursus cursus = new IndividueleCursus("testIndividueel2", 7);
		repository.create(cursus);
		assertEquals(aantalRecordsInCursussen + 1, super.countRowsInTable(CURSUSSEN));
		assertEquals(aantalRecordsInIndividueleCursussen + 1, super.countRowsInTable(INDIVIDUELE_CURSUSSEN));
		assertEquals(1, super.countRowsInTableWhere(CURSUSSEN, "id=" + cursus.getId()));
		assertEquals(1, super.countRowsInTableWhere(INDIVIDUELE_CURSUSSEN, "id=" + cursus.getId()));
	}
}
