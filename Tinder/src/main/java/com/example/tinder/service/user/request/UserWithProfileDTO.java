package com.example.tinder.service.user.request;

import com.example.tinder.model.Photo;
import com.example.tinder.model.interest.Interest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithProfileDTO {
    private Long id;
    private String username;


    private Long userProfileId;
    private String fullName;
    private String email;
    private String phone;
    private int age;

    private List<Photo> photos;
    private Set<Interest> interests;
    private String location;
}
