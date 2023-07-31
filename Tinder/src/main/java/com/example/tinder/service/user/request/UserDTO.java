package com.example.tinder.service.user.request;

import com.example.tinder.model.Photo;
import com.example.tinder.model.UserProfile;
import com.example.tinder.model.interest.Interest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private UserProfile userProfile;
    private List<Photo> photos;
    private Set<Interest> interests;
    private boolean vipaccount;
}
