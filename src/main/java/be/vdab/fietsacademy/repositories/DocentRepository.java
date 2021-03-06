package be.vdab.fietsacademy.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import be.vdab.fietsacademy.entities.Docent;
import be.vdab.fietsacademy.queryresults.AantalDocentenPerWedde;
import be.vdab.fietsacademy.queryresults.IdEnEmailAdres;

public interface DocentRepository {
	Optional<Docent> read(long id);
	void create(Docent docent);
	void delete(long id);
	List<Docent> findAll();
	List<Docent> findByWeddeBetween(BigDecimal van, BigDecimal tot);
	List<String> findEmailAdressen();
	List<IdEnEmailAdres> findIdsEnEmailAdressen();
	BigDecimal findGrootsteWedde();
	List<AantalDocentenPerWedde> findAantalDocentenPerWedde();
	int algemeneOpslag(BigDecimal percentage);
	Optional<Docent> readWithLock(long id);
}
