package com.example.tinder.service.auth.request;

import com.example.tinder.model.gender.Gender;
import com.example.tinder.model.interest.Interest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;

    private String phone;

    private String username;

    private String password;

    private String email;

    private String age;

    private String location;

    private Gender gender;

    private Interest interest;

    private List<String> photoUrls;

    private List<MultipartFile> photos;
}
