package com.example.tinder.repository;

import com.example.tinder.model.MatchedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchedUserRepository extends JpaRepository<MatchedUser, Long> {
}
