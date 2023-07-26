package com.example.tinder.controller.rest;

import com.example.tinder.model.Match;
import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.service.match.MatchService;
import com.example.tinder.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
@AllArgsConstructor
public class MatchRestController {
    private final MatchService matchService;

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getMatchesForCurrentUser(){

        List<User> matches = matchService.getMatchesForCurrentUser();
        List<Map<String, Object>> dataResponse = new ArrayList<>();

        for (User user : matches){
            Map<String, Object> data = new HashMap<>();

            data.put("id", user.getId());
            data.put("photo", user.getPhotos());
            data.put("fullname", user.getUserProfile().getFullName());
            dataResponse.add(data);
        }

        return ResponseEntity.ok(dataResponse);
    }
}
