package com.example.tinder.controller.rest;

import com.example.tinder.model.User;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiController {
    private final UserService userService;
    @GetMapping("/getReceiverId")
    public ResponseEntity<Long> getReceiverId(@RequestParam("username") String username) {
        User receiver = userService.findByUsername(username);
        if (receiver != null) {
            return ResponseEntity.ok(receiver.getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
