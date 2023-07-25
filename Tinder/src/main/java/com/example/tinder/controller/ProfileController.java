package com.example.tinder.controller;
import com.example.tinder.model.User;
import com.example.tinder.model.gender.Gender;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.interest.InterestService;
import com.example.tinder.service.user.UserProfileService;
import com.example.tinder.service.user.request.UserWithProfileDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping
@AllArgsConstructor
public class ProfileController {
    private final UserRepository userRepository;
    @Autowired
    private final UserProfileService userProfileService;
    private final InterestService interestService;
    @GetMapping("/profile")
    public String showProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameIgnoreCase(username);
        model.addAttribute("user", user);
        model.addAttribute("userprofile", user.getUserProfile());
        model.addAttribute("interests",user.getInterests());
        model.addAttribute("genders", Gender.values());
        return "profile/profile";
    }
    @GetMapping("/edit/{id}")
    public String editUserProfileForm(@PathVariable Long id, Model model) {
        UserWithProfileDTO userWithProfileDTO = userProfileService.getUserProfileWithUserId(id);
        model.addAttribute("userWithProfileDTO", userWithProfileDTO);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("allInterests", Interest.values());
        return "profile/profile";
    }
    @PostMapping("/edit")
    public String editUserProfile(@PathVariable Long userId, @ModelAttribute("userWithProfileDTO") UserWithProfileDTO updatedUserDTO) {
        userProfileService.editProfileUser(updatedUserDTO);
        return "redirect:/profile/profile/" + userId;
    }
}