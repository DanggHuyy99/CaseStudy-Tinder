package com.example.tinder.controller.rest;

import com.example.tinder.model.User;
import com.example.tinder.service.like.LikeService;
import com.example.tinder.service.like.request.LikeSaveRequest;
import com.example.tinder.service.match.MatchService;
import com.example.tinder.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/likes")
@AllArgsConstructor
public class LikeRestController {
    private final LikeService likeService;
    private final MatchService matchService;
    private final UserService userService;

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

    @GetMapping("/countLike")
    public ResponseEntity<Long> countLikeBYLikerAndDate(@RequestParam Long likerId) {
        long likeCount = likeService.countLikesByLikerAndCurrentDate(likerId);
        return ResponseEntity.ok(likeCount);
    }

//    @GetMapping("/countLike")
//    public ResponseEntity<Long> countLikeByLikerAndDate(@Valid @RequestBody LikeSaveRequest likeRequest) {
//        try {
//            LocalDate likeDate = LocalDate.parse(likeRequest.getDate());
//            User user = userService.findUserById(likeRequest.getLikerId());
//            Long likeCount = likeService.countLikesByLikerAndLikeDate(user, likeDate.atStartOfDay());
//            return ResponseEntity.ok(likeCount);
//        } catch (DateTimeParseException e) {
//            return ResponseEntity.badRequest().build();
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
