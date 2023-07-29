package com.example.tinder.controller.rest;

import com.example.tinder.model.User;
import com.example.tinder.service.like.SuperLikeService;
import com.example.tinder.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/superlikes")
@AllArgsConstructor
public class SuperLikeRestController {
    private final SuperLikeService superLikeService;
    private final UserService userService;

    @PostMapping("/superLike")
    public ResponseEntity<String> createSuperLike(@RequestParam Long likerId, @RequestParam Long likeeId) {
        User liker = userService.findUserById(likerId);
        User likee = userService.findUserById(likeeId);

        if (liker != null && likee != null) {
            boolean isVip = userService.isVipAccount(likerId);
            if (isVip) {
                superLikeService.createSuperLike(liker, likee);
                return ResponseEntity.ok("Super Like đã được tạo.");
            } else {
                return ResponseEntity.badRequest().body("Bạn cần là tài khoản VIP để sử dụng Super Like.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
