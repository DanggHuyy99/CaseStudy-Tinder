package com.example.tinder.service.user.request;

import com.example.tinder.model.gender.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileRequest {
    private Long id;

    private String fullName;

    private String phone;

    private String email;

    private String age;

    private String location;

    private Gender gender;

    private List<String> interest;

    private List<String> photoUrls;

    private List<MultipartFile> photos;
}
