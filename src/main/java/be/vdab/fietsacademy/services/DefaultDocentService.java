package be.vdab.fietsacademy.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import be.vdab.fietsacademy.entities.Docent;
import be.vdab.fietsacademy.exceptions.DocentNietGevondenException;
import be.vdab.fietsacademy.repositories.DocentRepository;

@Service
class DefaultDocentService implements DocentService {
	private final DocentRepository docentRepository;

	DefaultDocentService(DocentRepository docentRepository) {
		this.docentRepository = docentRepository;
	}
	
	@Override
	public void opslag(long id, BigDecimal percentage) {
		Optional<Docent> optionalDocent = docentRepository.read(id);
		if (optionalDocent.isPresent()) {
			optionalDocent.get().opslag(percentage);
		} else {
			throw new DocentNietGevondenException();
		}
	}
}
