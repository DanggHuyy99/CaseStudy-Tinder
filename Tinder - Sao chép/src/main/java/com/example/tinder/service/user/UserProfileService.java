package com.example.tinder.service.user;

import com.example.tinder.model.Photo;
import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.repository.UserProfileRepository;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.interest.InterestService;
import com.example.tinder.service.photo.PhotoService;
import com.example.tinder.service.user.request.ProfileRequest;
import com.example.tinder.service.user.request.UserWithProfileDTO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final InterestService interestService;
    private final PhotoService photoService;
    public UserProfile findUserProfileById(Long id){
        return userProfileRepository.findUserProfileByUserId(id);
    }

    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public void updateProfile (ProfileRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameIgnoreCase(username);
        UserProfile userProfile = user.getUserProfile();

        userProfile.setFullName(request.getFullName());
        userProfile.setEmail(request.getEmail());
        userProfile.setPhone(request.getPhone());
        userProfile.setAge(Integer.parseInt(request.getAge()));
        userProfile.setLocation(request.getLocation());
        userProfile.setGender(request.getGender());

        List<Photo> photos = new ArrayList<>();
        for (String url : request.getPhotoUrls()){
            Photo photo = new Photo();
            photo.setImageUrl("/assets/imgs/" + url);
            photo.setUser(user);
            photos.add(photo);
        }
        user.setPhotos(photos);

        List<String> interestNames = request.getInterest();
        Set<Interest> interests = new HashSet<>();
        for (String interestName : interestNames){
            Interest interest = Interest.valueOf(interestName);
            interests.add(interest);
        }
        user.setInterests(interests);
        userProfile.setUser(userRepository.save(user));
        userProfileRepository.save(userProfile);
    }

//    public UserWithProfileDTO editProfileUser(UserWithProfileDTO updatedUserDTO) {
//        Optional<User> user = userRepository.findById(updatedUserDTO.getId());
//        if (!user.isPresent()) {
//            throw new RuntimeException("Không tìm thấy người dùng với ID: " + updatedUserDTO.getId());
//        }
//        UserProfile userProfile = findUserProfileById(updatedUserDTO.getUserProfileId());
//        userProfile.setFullName(updatedUserDTO.getFullName());
//        userProfile.setEmail(updatedUserDTO.getEmail());
//        userProfile.setPhone(updatedUserDTO.getPhone());
//        userProfile.setAge(updatedUserDTO.getAge());
//        userProfile.setLocation(updatedUserDTO.getLocation());
//        Set<Interest> interests = interestService.getInterestByUserId(updatedUserDTO.getId());
//        interests.clear();
//        interests.addAll(updatedUserDTO.getInterests());
//        saveUserProfile(userProfile);
//        user.get().setUsername(updatedUserDTO.getUsername());
//        userRepository.save(user.get());
//        return updatedUserDTO;
//    }
//
//    public UserWithProfileDTO getUserProfileWithUserId(Long userId){
//        Optional<User> user = userRepository.findById(userId);
//        UserProfile userProfile = findUserProfileById(userId);
//        List<Photo> photos = photoService.getPhotosByUserId(userId);
//        Set<Interest> interests = interestService.getInterestByUserId(userId);
//        UserWithProfileDTO userWithProfileDTO = new UserWithProfileDTO();
//        userWithProfileDTO.setId(user.get().getId());
//        userWithProfileDTO.setUsername(user.get().getUsername());
//        userWithProfileDTO.setUserProfileId(userProfile.getId());
//        userWithProfileDTO.setFullName(userProfile.getFullName());
//        userWithProfileDTO.setEmail(userProfile.getEmail());
//        userWithProfileDTO.setPhone(userProfile.getPhone());
//        userWithProfileDTO.setAge(userProfile.getAge());
//        userWithProfileDTO.setPhotos(photos);
//        userWithProfileDTO.setInterests(interests);
//        userWithProfileDTO.setLocation(userProfile.getLocation());
//        return userWithProfileDTO;
//    }
}
