package be.vdab.fietsacademy.repositories;

import java.util.Optional;

import javax.persistence.EntityManager;

import be.vdab.fietsacademy.entities.Cursus;

class JpaCursusRepository implements CursusRepository {
	private final EntityManager manager;
	
	public JpaCursusRepository(EntityManager manager) {
		this.manager = manager;
	}
	
	@Override
	public Optional<Cursus> read(String id) {
		return Optional.ofNullable(manager.find(Cursus.class, id));
	}
	@Override
	public void create(Cursus cursus) {
		manager.persist(cursus);
	}
}
