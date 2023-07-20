package com.example.tinder.controller.rest;

import com.example.tinder.model.Photo;
import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.service.interest.InterestService;
import com.example.tinder.service.photo.PhotoService;
import com.example.tinder.service.user.UserProfileService;
import com.example.tinder.service.user.UserService;
import com.example.tinder.service.user.request.UserWithProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/userprofiles")
@AllArgsConstructor
@Data
public class UserWithProfileRestController {
    private final UserService userService;

    private final UserProfileService userProfileService;

    private final PhotoService photoService;

    private final InterestService interestService;

    @GetMapping
    public ResponseEntity<List<UserWithProfileDTO>> getAllUserWithProfile(){
        List<UserWithProfileDTO> userWithProfileDTOS = userService.getAllUserWithProfileDTO();
        return ResponseEntity.ok(userWithProfileDTOS);
    }
    private final
    @GetMapping("/{userId}") ResponseEntity<UserWithProfileDTO> getUserWithProfile(@PathVariable Long userId) {
//        User user = userService.findUserById(userId);
//        UserProfile userProfile = userProfileService.findUserProfileById(userId);
//        List<Photo> photos = photoService.getPhotosByUserId(userId);
//        Set<Interest> interests = interestService.getInterestByUserId(userId);
//
//        UserWithProfileDTO userWithProfileDTO = new UserWithProfileDTO();
//        userWithProfileDTO.setId(user.getId());
//        userWithProfileDTO.setUsername(user.getUsername());
//
//
//        userWithProfileDTO.setUserProfileId(userProfile.getId());
//        userWithProfileDTO.setFullName(userProfile.getFullName());
//        userWithProfileDTO.setEmail(userProfile.getEmail());
//        userWithProfileDTO.setPhone(userProfile.getPhone());
//        userWithProfileDTO.setAge(userProfile.getAge());
//        userWithProfileDTO.setPhotos(photos);
//        userWithProfileDTO.setInterests(interests);
//        userWithProfileDTO.setLocation(userProfile.getLocation());

        UserWithProfileDTO userWithProfileDTO = userService.getUserProfileWithUserId(userId);
        return ResponseEntity.ok(userWithProfileDTO);
    }
}
