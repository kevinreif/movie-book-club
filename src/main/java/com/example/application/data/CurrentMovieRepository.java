package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentMovieRepository extends JpaRepository<CurrentMovie, Long>, JpaSpecificationExecutor<CurrentMovie> {

    CurrentMovie findByTitle(String title);

    @Query("select c from CurrentMovie c where currentMovie = true")
    CurrentMovie getCurrent();
}