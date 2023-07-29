package com.example.tinder.repository;

import com.example.tinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCaseOrUserProfileEmailIgnoreCaseOrUserProfilePhoneIgnoreCase(String username, String userProfile_email, String userProfile_phone);

    public User findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByUserProfileEmail(String userProfile_email);

    boolean existsByUserProfilePhone(String userProfile_phone);

    User findByUserProfileEmailAndIdNot(String userProfile_email, Long id);

    User findByUserProfilePhoneAndIdNot(String userProfile_phone, Long id);

    @Query(value = "SELECT * FROM user u WHERE  u.id not in (SELECT likee_id from user_like l where liker_id = :id group by likee_id)", nativeQuery = true)
    List<User> findUserNotLikeOrMatch(Long id);
}
