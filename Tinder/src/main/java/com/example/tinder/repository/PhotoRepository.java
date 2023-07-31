package com.example.tinder.repository;

import com.example.tinder.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUserId(Long user_id);

    @Modifying
    @Query(value = "delete FROM Photo WHERE id = :id")
    void delDe(Long id);
}
