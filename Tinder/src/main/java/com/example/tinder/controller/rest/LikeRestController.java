package com.example.tinder.controller.rest;

import com.example.tinder.service.like.LikeService;
import com.example.tinder.service.like.request.LikeSaveRequest;
import com.example.tinder.service.match.MatchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@AllArgsConstructor
public class LikeRestController {
    private final LikeService likeService;
    private final MatchService matchService;

    @PostMapping("/like")
    public ResponseEntity<Boolean> likeUser (@RequestBody LikeSaveRequest likeSaveRequest){
        try {
            likeService.saveLike(likeSaveRequest);

            if (matchService.isMatched(likeSaveRequest.getLikerId(), likeSaveRequest.getLikeeId())){
                matchService.createMatch(likeSaveRequest.getLikerId(), likeSaveRequest.getLikeeId());
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.ok(false);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
