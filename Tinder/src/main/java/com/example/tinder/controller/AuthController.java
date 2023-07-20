package com.example.tinder.controller;

import com.example.tinder.model.gender.Gender;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.service.auth.AuthService;
import com.example.tinder.service.auth.request.RegisterRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Controller
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    @GetMapping("/register")
    public String showRegister(Model model) {
        RegisterRequest request = new RegisterRequest();
        model.addAttribute("user", request);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("interests", Interest.values());
        return "/user/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("fileUpload") MultipartFile[] fileUpload, @Valid @ModelAttribute("user") RegisterRequest request, BindingResult result, Model model) throws IOException {
        authService.checkUsernameOrPhoneNumberOrEmail(request, result);
//loop qua làm từng sau lưu xuông database/
        List<String> listImg = new ArrayList<>();
        for (var file : fileUpload) {
            if (file.isEmpty()) {
                continue;
            }
                //save img to db
                String name = file.getOriginalFilename();
                listImg.add(name);
                request.setPhotoUrls(listImg);

                // Lấy đường dẫn tới thư mục gốc của dự án
                String projectDir = System.getProperty("user.dir");

                // Lưu file vào thư mục uploads
                String fileName = file.getOriginalFilename();

                // Đường dẫn đến thư mục lưu trữ file trong thư mục resources
                String uploadDir = projectDir + "/src/main/resources/assets/imgs/";

                // Tạo thư mục nếu chưa tồn tại
                Path  directory = Path.of(uploadDir);
                if (!Files.exists(directory)) {
                    Files.createDirectory(directory,null);
                }
                Path targetPath = directory.resolve(fileName);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        }

        model.addAttribute("interests", Interest.values());
        model.addAttribute("genders", Gender.values());
        if (result.hasErrors()) {
            return "/user/register";
        }
//        List<String> photoUrls = request.getPhotoUrls();
        authService.register(request);
        return "redirect:/register?success";
    }
}
