package com.example.tinder.repository;

import com.example.tinder.model.Like;
import com.example.tinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByLikee_IdAndLiker_Id(Long likee_id, Long liker_id);

//    Long countByLikerAndLikeDate(User liker, LocalDateTime likeDate);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.liker.id = ?1 AND DATE('like_date') = ?2")
    long countLikesByLikerAndLikeDate(Long likerId, LocalDateTime likeDate);

    @Query(value = "SELECT COUNT(*) FROM user_like WHERE liker_id = ?1 AND DATE_FORMAT(like_date, '%W %M %e %Y') = DATE_FORMAT(now(), '%W %M %e %Y')", nativeQuery = true)
    long countLikesByLikerAndCurrentDate(Long likerId);

    boolean existsByLikerIdAndLikeeId(Long liker_id, Long likee_id);
}
