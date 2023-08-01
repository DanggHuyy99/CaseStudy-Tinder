package com.example.tinder.repository;

import com.example.tinder.model.Match;
import com.example.tinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findMatchesByUser1_IdOrUser2_Id(Long user1_id, Long user2_id);

    boolean existsByUser1IdAndUser2Id(Long user1_id, Long user2_id);

    Optional<Match> findByUser1IdAndUser2Id(Long user1_id, Long user2_id);
}
