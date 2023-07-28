package com.example.tinder.controller;

import com.example.tinder.repository.PhotoRepository;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.user.UserProfileService;
import com.example.tinder.service.user.UserService;
import com.example.tinder.service.user.request.ProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping
@AllArgsConstructor
public class Profile1Controller {
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final PhotoRepository photoRepository;
    private final UserService userService;


//    @PostMapping("/profile")
//    public String updateProfile(ProfileRequest userprofile, Model model, BindingResult result) throws IOException {
//        FieldError error = new FieldError("profileRequest", "email", "There is already an account registered with the same email");
//        result.addError(error);
//        model.
//
//        model.addAttribute("userprofile", userprofile);
//        return "/profile/error";
//    }

}