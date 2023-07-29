package com.example.tinder.controller.rest;

import com.example.tinder.service.like.LikeService;
import com.example.tinder.service.match.MatchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LikeOrMatchRestController {
    private LikeService likeService;

    private MatchService matchService;

    @GetMapping("/hasLikedOrMatched")
    public ResponseEntity<Boolean> hasLikedOrMatched(
            @RequestParam Long likerId,
            @RequestParam Long profileId
    ) {
        boolean hasLiked = likeService.hasLiked(likerId, profileId);
        boolean hasMatched = matchService.hasMatched(likerId, profileId);

        // Trả về true nếu người dùng đã like hoặc match, ngược lại trả về false
        boolean result = hasLiked || hasMatched;
        return ResponseEntity.ok(result);
    }
}
