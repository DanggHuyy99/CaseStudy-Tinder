package com.example.tinder.controller.rest;

import com.example.tinder.model.User;
import com.example.tinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Data
public class UserRestController {
    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
