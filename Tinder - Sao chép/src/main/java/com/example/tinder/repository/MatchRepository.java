package com.example.tinder.repository;

import com.example.tinder.model.Match;
import com.example.tinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findMatchesByUser1_IdOrUser2_Id(Long user1_id, Long user2_id);
}
