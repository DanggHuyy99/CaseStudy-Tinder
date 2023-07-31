package com.example.tinder.repository;

import com.example.tinder.model.User;
import com.example.tinder.model.interest.Interest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCaseOrUserProfileEmailIgnoreCaseOrUserProfilePhoneIgnoreCase(String username, String userProfile_email, String userProfile_phone);

    @EntityGraph(attributePaths = {"interests", "photos"})
    Optional<List<User>> findByInterests(Interest interest);
    public User findByUsernameIgnoreCase(String username);

    User findAllByUserProfileAge(int userProfile_age_min, int userProfile_age_max);
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByUserProfileEmail(String userProfile_email);

    boolean existsByUserProfilePhone(String userProfile_phone);

    User findByUserProfileEmailAndIdNot(String userProfile_email, Long id);

    User findByUserProfilePhoneAndIdNot(String userProfile_phone, Long id);

    @Query(value = "SELECT * FROM user u WHERE  u.id not in (SELECT likee_id from user_like l where liker_id = :id group by likee_id)", nativeQuery = true)
    List<User> findUserNotLikeOrMatch(Long id);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.userProfile " +
            "LEFT JOIN FETCH u.photos " +
            "WHERE :interest MEMBER OF u.interests")
    Optional<List<User>> findByInterestsFetchProfileAndPhotos(@Param("interest") Interest interest);
}
