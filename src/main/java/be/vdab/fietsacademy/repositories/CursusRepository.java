package be.vdab.fietsacademy.repositories;

import java.util.Optional;

import be.vdab.fietsacademy.entities.Cursus;

public interface CursusRepository {
	Optional<Cursus> read(String id);
	void create(Cursus cursus);
}
