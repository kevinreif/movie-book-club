package com.example.application.services;

import com.example.application.data.*;
import jakarta.persistence.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ReservedMovieService {
    private final ReservedMovieRepository repository;

    public ReservedMovieService(ReservedMovieRepository repository) {
        this.repository = repository;
    }

    public Optional<ReservedMovie> get(Long id) {
        return repository.findById(id);
    }

    public ReservedMovie update(ReservedMovie entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<ReservedMovie> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ReservedMovie> list(Pageable pageable, Specification<ReservedMovie> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}

