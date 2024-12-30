package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservedMovieRepository extends JpaRepository<ReservedMovie, Long>, JpaSpecificationExecutor<ReservedMovie> {

    ReservedMovie findByTitle(String title);

    @Query("select count(r) from ReservedMovie r where r.picker = :picker")
    int countByPicker(String picker);
}