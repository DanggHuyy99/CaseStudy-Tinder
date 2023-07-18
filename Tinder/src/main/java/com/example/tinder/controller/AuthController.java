package com.example.tinder.controller;

import com.example.tinder.model.gender.Gender;
import com.example.tinder.service.AuthService;
import com.example.tinder.service.auth.request.RegisterRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    @GetMapping("/register")
    public String showRegister(Model model) {
        RegisterRequest request = new RegisterRequest();
        model.addAttribute("user", request);
        model.addAttribute("genders", Gender.values());
        return "/user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterRequest request, BindingResult result, Model model) {
        authService.checkUsernameOrPhoneNumberOrEmail(request, result);
        model.addAttribute("genders", Gender.values());
        if (result.hasErrors()) {
            return "/user/register";
        }
        authService.register(request);
        return "redirect:/register?success";
    }
}
