package com.example.tinder.service.interest;

import com.example.tinder.model.User;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@AllArgsConstructor
@Data
public class InterestService {
    private final UserRepository userRepository;

    public Set<Interest> getInterestByUserId(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user.getInterests();
        } else {
            return Collections.emptySet();
        }
    }
}
