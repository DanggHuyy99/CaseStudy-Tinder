package com.example.tinder.service.user;

import com.example.tinder.model.UserProfile;
import com.example.tinder.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    public UserProfile findUserProfileById(Long id){
        return userProfileRepository.findUserProfileByUserId(id);
    }
}
