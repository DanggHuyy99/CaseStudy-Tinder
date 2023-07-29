package com.example.tinder.controller.rest;

import com.example.tinder.model.Photo;
import com.example.tinder.model.User;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Data
public class UserRestController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getPhoto")
    public ResponseEntity<User> getPhoto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameIgnoreCase(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/searchByAge")
    public ResponseEntity<?> searchByAge(@RequestParam("minAge") int minAge, @RequestParam("maxAge") int maxAge) {
        User users = userService.findUsersByAgeRange(minAge, maxAge);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getPhotoById")
    public ResponseEntity<?> getPhotoById(@RequestParam Long id) {

        User user = userRepository.findById(id).get();
        return ResponseEntity.ok(user.getPhotos());
    }

    @PostMapping("/upgradeAccount")
    public ResponseEntity<?> upgradeAccount(@RequestParam Long userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại");
        }
        user.setVIPaccount(true);
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isVipAccount")
    public ResponseEntity<Boolean> isVipAccount(@RequestParam Long userId) {
        boolean isVip = userService.isVipAccount(userId);
        return ResponseEntity.ok(isVip);
    }
    @GetMapping("/getUserCategory/{interest}")
    public  ResponseEntity<List<User>> getUserByCategory(@PathVariable Interest interest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
        List<User> user = userRepository.findByInterests(interest).get();
        return ResponseEntity.ok(user);
    }
}
