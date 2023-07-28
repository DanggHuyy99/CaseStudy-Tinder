package com.example.tinder.service.like;

import com.example.tinder.model.Like;
import com.example.tinder.model.User;
import com.example.tinder.repository.LikeRepository;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.like.request.LikeSaveRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Data
public class LikeService {
    private final LikeRepository likeRepository;

    private final UserRepository userRepository;

    public void saveLike(LikeSaveRequest likeSaveRequest) {
        Optional<User> liker = userRepository.findById(likeSaveRequest.getLikerId());
        Optional<User> likee = userRepository.findById(likeSaveRequest.getLikeeId());

        if (liker.isPresent() && likee.isPresent()) {
            Like like = new Like();
            like.setLiker(liker.get());
            like.setLikee(likee.get());
            like.setLikeDate(LocalDateTime.now());
            likeRepository.save(like);
        } else {
            throw new RuntimeException("Invalid user");
        }
    }

    public long countLikesByLikerAndCurrentDate(Long likerId) {
        return likeRepository.countLikesByLikerAndCurrentDate(likerId);
    }
}
