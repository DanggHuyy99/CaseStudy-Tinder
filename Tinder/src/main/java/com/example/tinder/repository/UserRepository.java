package com.example.tinder.repository;

import com.example.tinder.model.User;
import com.example.tinder.model.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCaseOrUserProfileEmailIgnoreCaseOrUserProfilePhoneIgnoreCase(String username, String userProfile_email, String userProfile_phone);
    Optional<List<User>> findByInterests(Interest interest);
    public User findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByUserProfileEmail(String userProfile_email);

    boolean existsByUserProfilePhone(String userProfile_phone);

    User findByUserProfileEmailAndIdNot(String userProfile_email, Long id);

    User findByUserProfilePhoneAndIdNot(String userProfile_phone, Long id);
}
