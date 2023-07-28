package com.example.tinder.service.user;

import com.example.tinder.model.Photo;
import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.interest.InterestService;
import com.example.tinder.service.photo.PhotoService;
import com.example.tinder.service.user.request.UserWithProfileDTO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final PhotoService photoService;
    private final InterestService interestService;

    public User findByUsername(String username){
        return userRepository.findByUsernameIgnoreCase(username);
    }
    public User findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserWithProfileDTO> getAllUserWithProfileDTO() {
        List<User> users = userRepository.findAll();
        List<UserWithProfileDTO> userWithProfileDTOS = new ArrayList<>();

        for (User user : users){
            UserProfile userProfile = user.getUserProfile();
            List<Photo> photos = user.getPhotos();
            Set<Interest> interests = user.getInterests();

            UserWithProfileDTO userWithProfileDTO = new UserWithProfileDTO();
            userWithProfileDTO.setId(user.getId());
            userWithProfileDTO.setUsername(user.getUsername());

            if (userProfile != null) {
                userWithProfileDTO.setUserProfileId(userProfile.getId());
                userWithProfileDTO.setFullName(userProfile.getFullName());
                userWithProfileDTO.setEmail(userProfile.getEmail());
                userWithProfileDTO.setPhone(userProfile.getPhone());
                userWithProfileDTO.setAge(userProfile.getAge());
                userWithProfileDTO.setGender(userProfile.getGender());
                userWithProfileDTO.setLocation(userProfile.getLocation());
            }
            userWithProfileDTO.setPhotos(photos);
            userWithProfileDTO.setInterests(interests);

            userWithProfileDTOS.add(userWithProfileDTO);
        }
        return userWithProfileDTOS;
    }

    public UserWithProfileDTO getUserProfileWithUserId(Long userId){
        Optional<User> user = userRepository.findById(userId);
        UserProfile userProfile = userProfileService.findUserProfileById(userId);
        List<Photo> photos = photoService.getPhotosByUserId(userId);
        Set<Interest> interests = interestService.getInterestByUserId(userId);

        UserWithProfileDTO userWithProfileDTO = new UserWithProfileDTO();
        userWithProfileDTO.setId(user.get().getId());
        userWithProfileDTO.setUsername(user.get().getUsername());


        userWithProfileDTO.setUserProfileId(userProfile.getId());
        userWithProfileDTO.setFullName(userProfile.getFullName());
        userWithProfileDTO.setEmail(userProfile.getEmail());
        userWithProfileDTO.setPhone(userProfile.getPhone());
        userWithProfileDTO.setAge(userProfile.getAge());
        userWithProfileDTO.setPhotos(photos);
        userWithProfileDTO.setInterests(interests);
        userWithProfileDTO.setLocation(userProfile.getLocation());

        return userWithProfileDTO;
    }

    public boolean isVipAccount(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.isVIPaccount();
        }
        return false;
    }
}
