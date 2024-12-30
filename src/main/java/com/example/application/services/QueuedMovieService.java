package com.example.application.services;

import com.example.application.data.Movie;
import com.example.application.data.QueuedMovie;
import com.example.application.data.QueuedMovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QueuedMovieService {
    private final QueuedMovieRepository repository;

    public QueuedMovieService(QueuedMovieRepository repository) {
        this.repository = repository;
    }

    public Optional<QueuedMovie> get(Long id) {
        return repository.findById(id);
    }

    public QueuedMovie update(QueuedMovie entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<QueuedMovie> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<QueuedMovie> list(Pageable pageable, Specification<QueuedMovie> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
