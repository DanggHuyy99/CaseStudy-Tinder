package com.example.tinder.repository;

import com.example.tinder.model.Like;
import com.example.tinder.model.Superlike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperlikeRepository extends JpaRepository<Superlike, Long> {
}
