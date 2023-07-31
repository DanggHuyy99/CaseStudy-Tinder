package com.example.tinder.controller;
import com.example.tinder.model.Photo;
import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.model.gender.Gender;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.repository.PhotoRepository;
import com.example.tinder.repository.UserProfileRepository;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.auth.AuthService;
import com.example.tinder.service.auth.request.RegisterRequest;
import com.example.tinder.service.interest.InterestService;
import com.example.tinder.service.photo.PhotoService;
import com.example.tinder.service.user.UserProfileService;
import com.example.tinder.service.user.UserService;
import com.example.tinder.service.user.request.ProfileRequest;
import com.example.tinder.service.user.request.UserWithProfileDTO;
import com.example.tinder.ulti.AppUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
@RequestMapping
@AllArgsConstructor
public class ProfileController {
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final PhotoRepository photoRepository;
    private final UserService userService;
    @GetMapping("/profile")
    public String showProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameIgnoreCase(username);
        model.addAttribute("user", user);
        model.addAttribute("userprofile", user.getUserProfile());
        model.addAttribute("interests",user.getInterests());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("allInterests", Interest.values());
        return "profile/profile";
    }

    @PostMapping("/profile")
    @Transactional
    public String updateProfile(@RequestParam("fileUpload") MultipartFile[] fileUpload, @Valid @ModelAttribute("request") ProfileRequest request,@ModelAttribute("userprofile") UserProfile userprofile,  BindingResult result, Model model) throws IOException {
        userProfileService.checkProfileUser(request, result);

        // Xóa ảnh cũ
        User user = userService.getCurrentUser();
        List<Photo> oldPhotos = user.getPhotos();
        List<String> listImg = new ArrayList<>();
        // for qua list fileUpload có fileName thì lấy index rồi xóa trong photos của usẻr
        for (int i = 0; i < fileUpload.length; i++) {
            if (!fileUpload[i].getOriginalFilename().equals("")){
                if ( i <= oldPhotos.size() - 1 ) {
                    //xoa database
                    if ( i <= oldPhotos.size() - 1 ) {
                        Long id = oldPhotos.get(i).getId();
                        photoRepository.delDe(id);

                    }
                    //them moi
                    String projectDir = System.getProperty("user.dir");
                    String fileName = fileUpload[i].getOriginalFilename();
                    String uploadDir = projectDir + "/src/main/resources/assets/imgs/";
                    Path directory = Path.of(uploadDir);
                    if (!Files.exists(directory)) {
                        Files.createDirectory(directory, null);
                    }
                    Path targetPath = directory.resolve(fileName);
                    Files.copy(fileUpload[i].getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                    // Thêm tên ảnh mới vào danh sách tên ảnh
                    listImg.add(fileName);

                } else {
                    String projectDir = System.getProperty("user.dir");
                    String fileName = fileUpload[i].getOriginalFilename();
                    String uploadDir = projectDir + "/src/main/resources/assets/imgs/";
                    Path directory = Path.of(uploadDir);
                    if (!Files.exists(directory)) {
                        Files.createDirectory(directory, null);
                    }
                    Path targetPath = directory.resolve(fileName);
                    Files.copy(fileUpload[i].getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                    // Thêm tên ảnh mới vào danh sách tên ảnh
                    listImg.add(fileName);
                }
            }
        }

        request.setPhotoUrls(listImg);

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            userprofile =  user.getUserProfile();
            model.addAttribute("interests",user.getInterests());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("allInterests", Interest.values());
            return "/profile/error";
        }
        userProfileService. updateProfile(request);
        return "redirect:/profile";
    }

}