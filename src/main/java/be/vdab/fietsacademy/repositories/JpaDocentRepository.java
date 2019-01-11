package be.vdab.fietsacademy.repositories;

import java.util.Optional;

import javax.persistence.EntityManager;

import be.vdab.fietsacademy.entities.Docent;

class JpaDocentRepository implements DocentRepository {
	private final EntityManager manager;
	JpaDocentRepository(EntityManager manager) {
		this.manager = manager;
	}
	@Override
	public Optional<Docent> read(long id) {
		return Optional.ofNullable(manager.find(Docent.class, id));
	}	
}
