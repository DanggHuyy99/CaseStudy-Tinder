package com.example.tinder.service.user;

import com.example.tinder.model.UserProfile;
import com.example.tinder.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    public UserProfile findUserProfileById(Long id){
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(id);
        return optionalUserProfile.orElse(null);
    }
}
