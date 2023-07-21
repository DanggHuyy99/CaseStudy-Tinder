package com.example.tinder.repository;

import com.example.tinder.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByLikee_IdAndLiker_Id(Long likee_id, Long liker_id);
}
