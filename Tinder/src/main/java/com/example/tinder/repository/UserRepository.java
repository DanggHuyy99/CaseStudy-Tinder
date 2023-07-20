package com.example.tinder.repository;

import com.example.tinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCaseOrUserProfileEmailIgnoreCaseOrUserProfilePhoneIgnoreCase(String username, String userProfile_email, String userProfile_phone);

    public User findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByUserProfileEmail(String userProfile_email);

    boolean existsByUserProfilePhone(String userProfile_phone);
}
