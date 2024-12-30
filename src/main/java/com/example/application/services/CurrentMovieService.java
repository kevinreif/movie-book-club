package com.example.application.services;

import com.example.application.data.CurrentMovie;
import com.example.application.data.CurrentMovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentMovieService {
    private final CurrentMovieRepository repository;

    public CurrentMovieService(CurrentMovieRepository repository) {
        this.repository = repository;
    }

    public Optional<CurrentMovie> get(Long id) {
        return repository.findById(id);
    }

    public CurrentMovie update(CurrentMovie entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<CurrentMovie> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<CurrentMovie> list(Pageable pageable, Specification<CurrentMovie> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
