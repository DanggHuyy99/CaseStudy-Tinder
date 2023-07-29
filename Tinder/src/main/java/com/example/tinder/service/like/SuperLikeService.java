package com.example.tinder.service.like;

import com.example.tinder.model.SuperLikee;
import com.example.tinder.model.User;
import com.example.tinder.repository.SuperLikeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class SuperLikeService {
    private final SuperLikeeRepository superLikeeRepository;
    public void createSuperLike(User liker, User likee) {
        SuperLikee superLike = new SuperLikee();
        superLike.setLiker(liker);
        superLike.setLikee(likee);
        superLike.setSuperLikeDate(LocalDateTime.now());
        superLikeeRepository.save(superLike);
    }
}
