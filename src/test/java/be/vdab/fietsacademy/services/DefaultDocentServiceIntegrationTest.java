package be.vdab.fietsacademy.services;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/insertCampus.sql")
@Sql("/insertDocent.sql")
public class DefaultDocentServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private DefaultDocentService service;
	@Autowired
	private EntityManager manager;
	
	private long idVanTestMan() {
		return super.jdbcTemplate.queryForObject("select id from docenten where voornaam='testM';", Long.class);
	}
	@Test
	public void opslag() {
		long id = idVanTestMan();
		service.opslag(id, BigDecimal.TEN);
		manager.flush();
		BigDecimal nieuweWedde = super.jdbcTemplate.queryForObject("select wedde from docenten where id=?;", BigDecimal.class, id);
		assertEquals(0, BigDecimal.valueOf(1_100).compareTo(nieuweWedde));
	}

}
