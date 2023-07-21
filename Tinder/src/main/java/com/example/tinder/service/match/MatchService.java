package com.example.tinder.service.match;

import com.example.tinder.model.Match;
import com.example.tinder.model.Photo;
import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MatchService {
    private final UserRepository userRepository;

    private final MatchRepository matchRepository;

    private final LikeRepository likeRepository;

    private final UserProfileRepository userProfileRepository;

    private final PhotoRepository photoRepository;

    public void createMatch(Long likerId, Long likeeId) {
        User liker = userRepository.findById(likerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid likerId"));
        User likee = userRepository.findById(likeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid likeeId"));

        Match match = new Match();
        match.setUser1(liker);
        match.setUser2(likee);
        match.setMatchDate(LocalDateTime.now());
        matchRepository.save(match);
    }

    public boolean isMatched(Long likerid, Long likeeid) {
        //người ược like có đang like người đang đăng nhập không?

        return likeRepository.existsByLikee_IdAndLiker_Id(likerid, likeeid);
    }

    public List<User> getMatchesForCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsernameIgnoreCase(username);



        List<Match> matchesForCurrentUser = matchRepository.findMatchesByUser1_IdOrUser2_Id(currentUser.getId(),currentUser.getId());
        List<User> userMatches = new ArrayList<>();
        for (Match match :matchesForCurrentUser)  {
            if(match.getUser1().getId()==currentUser.getId()){
                UserProfile userProfile = userProfileRepository.findUserProfileByUserId(match.getUser2().getId());
                List<Photo> photos = photoRepository.findByUserId(match.getUser2().getId());
                match.getUser2().setUserProfile(userProfile);
                match.getUser2().setPhotos(photos);
                userMatches.add(match.getUser2());
            } else {
                UserProfile userProfile = userProfileRepository.findUserProfileByUserId(match.getUser1().getId());
                List<Photo> photos = photoRepository.findByUserId(match.getUser1().getId());
                match.getUser1().setUserProfile(userProfile);
                match.getUser1().setPhotos(photos);
                userMatches.add(match.getUser1());
            }
        }

        return userMatches;
    }
}
