package com.example.tinder.service.user;

import com.example.tinder.model.Photo;
import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.repository.UserProfileRepository;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.auth.request.RegisterRequest;
import com.example.tinder.service.interest.InterestService;
import com.example.tinder.service.photo.PhotoService;
import com.example.tinder.service.user.request.ProfileRequest;
import com.example.tinder.service.user.request.UserWithProfileDTO;
import com.example.tinder.validate.Validate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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

    public boolean checkProfileUser(ProfileRequest request, BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameIgnoreCase(username);
        User checkEmail = userRepository.findByUserProfileEmailAndIdNot(request.getEmail(), user.getId());
        User checkPhone = userRepository.findByUserProfilePhoneAndIdNot(request.getPhone(), user.getId());
        boolean check = false;
        if (request.getEmail().equals("")) {
            FieldError error = new FieldError("userprofile", "email", "Email can't be empty");
            bindingResult.addError(error);
            check = true;
        }
        else if (checkEmail != null) {
            FieldError error = new FieldError("userprofile", "email", "There is already an account registered with the same email");
            bindingResult.addError(error);
            check = true;
        }
        else if (!Validate.isEmailValid(request.getEmail())) {
            FieldError error = new FieldError("userprofile", "email", "Invalid email (domain name only supports .com|edu|net|org|biz|info|pro) or country domain name for example .vn,...");
            bindingResult.addError(error);
            check = true;
        }
        if (request.getPhone().equals("")) {
            FieldError error = new FieldError("userprofile", "phone", "Phone can't be empty");
            bindingResult.addError(error);
            check = true;
        } else if (checkPhone != null) {
            FieldError error = new FieldError("userprofile", "phone", "There is already an account registered with the same phone number");
            bindingResult.addError(error);
            check = true;
        } else if (!Validate.isPhoneValid(request.getPhone())) {
            FieldError error = new FieldError("userprofile", "phone", "Phone number must have 10 digits and start with 0");
            bindingResult.addError(error);
            check = true;
        }
        return check;
    }
}
