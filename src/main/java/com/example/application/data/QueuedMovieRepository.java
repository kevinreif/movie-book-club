package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueuedMovieRepository extends JpaRepository<QueuedMovie, Long>, JpaSpecificationExecutor<QueuedMovie> {

    QueuedMovie findByTitle(String title);

    @Query("select count(q) from QueuedMovie q where q.picker = :picker")
    int countByPicker(String picker);

    @Query("select id(q) from QueuedMovie q")
    List<Long> getAllIDs();

    @Query("select q from QueuedMovie q where q.id = :id")
    QueuedMovie getByID(Long id);

    @Query("select posterURL from QueuedMovie q")
    List<String> getAllPosters();

    @Query("select title from QueuedMovie q")
    List<String> getAllTitles();

    @Query("select q from QueuedMovie q where q.title = :title")
    QueuedMovie getByTitle(String title);

    @Query("select posterURL from QueuedMovie q where q.title = :title")
    String getPosterByTitle(String title);


}
