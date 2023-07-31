package com.example.tinder.service.auth;

import com.example.tinder.model.Photo;
import com.example.tinder.model.Preference;
import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.model.gender.Gender;
import com.example.tinder.model.interest.Interest;
import com.example.tinder.model.role.Role;
import com.example.tinder.repository.PhotoRepository;
import com.example.tinder.repository.UserProfileRepository;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.auth.request.RegisterRequest;
import com.example.tinder.ulti.AppUtils;
import com.example.tinder.validate.Validate;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passworEncoder;

    private final UserProfileRepository userProfileRepository;

    private final PhotoRepository photoRepository;

    public void register( RegisterRequest request) {
        var user = AppUtils.mapper.map(request, User.class);
        user.setVIPaccount(false);
        user.setPassword(passworEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);


        UserProfile userProfile = new UserProfile();
        userProfile.setFullName(request.getFullName());
        userProfile.setEmail(request.getEmail());
        userProfile.setPhone(request.getPhone());
        userProfile.setAge(Integer.parseInt(request.getAge()));
        userProfile.setLocation(request.getLocation());
        userProfile.setGender(request.getGender());

        Preference preference = new Preference();
        preference.setUser(user);
        preference.setMinAge(16);
        preference.setMaxAge(69);
        if (request.getGender() == Gender.FEMALE) {
            preference.setGender(Gender.MALE);
        } else if (request.getGender() == Gender.MALE){
            preference.setGender(Gender.FEMALE);
        } else {
            preference.setGender(Gender.OTHER);
        }
        user.setPreference(preference);

        List<Photo> photos = new ArrayList<>();
        for (String url : request.getPhotoUrls()){
            Photo photo = new Photo();
            photo.setImageUrl("/assets/imgs/" + url);
            photo.setUser(user);
            photos.add(photo);
        }
        user.setPhotos(photos);

        List<String> interestNames = request.getInterest();
        Set<Interest> interests = new HashSet<>();
        for (String interestName : interestNames){
            Interest interest = Interest.valueOf(interestName);
            interests.add(interest);
        }
        user.setInterests(interests);
        userProfile.setUser(userRepository.save(user));
        userProfileRepository.save(userProfile);
    }

    public boolean checkUsernameOrPhoneNumberOrEmail(RegisterRequest request, BindingResult bindingResult) {
        boolean check = false;
        if (request.getUsername().equals("")) {
            FieldError error = new FieldError("user", "username", "Username cannot be empty");
            bindingResult.addError(error);
            check = true;
        } else if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            FieldError error = new FieldError("user", "username", "There is already an account registered with the same username");
            bindingResult.addError(error);
            check = true;
        }else if (!Validate.isUsernameValid(request.getUsername())) {
            FieldError error = new FieldError("user", "username", "The username can't be longer than 15 characters and can't have special characters");
            bindingResult.addError(error);
            check = true;
        }
        if (request.getPassword().equals("")) {
            FieldError error = new FieldError("user", "password", "Password cannot be empty");
            bindingResult.addError(error);
            check = true;
        } else if (!Validate.isPasswordValid(request.getPassword())) {
            FieldError error = new FieldError("user", "password", "Password must not contain special characters and must not exceed 15 characters");
            bindingResult.addError(error);
            check = true;
        }
        if (request.getFullName().equals("")) {
            FieldError error = new FieldError("user", "fullName", "FullName cannot be empty");
            bindingResult.addError(error);
            check = true;
        } else if (!Validate.isFullNameValid(request.getFullName())) {
            FieldError error = new FieldError("user", "fullName", "The FullName cannot contain special characters, leading and trailing spaces");
            bindingResult.addError(error);
            check = true;
        }
        if (request.getEmail().equals("")) {
            FieldError error = new FieldError("user", "email", "Email cannot be empty");
            bindingResult.addError(error);
            check = true;
        } else if (userRepository.existsByUserProfileEmail(request.getEmail())) {
            FieldError error = new FieldError("user", "email", "There is already an account registered with the same email");
            bindingResult.addError(error);
            check = true;
        } else if (!Validate.isEmailValid(request.getEmail())) {
            FieldError error = new FieldError("user", "email", "Invalid email (domain name only supports .com|edu|net|org|biz|info|pro) or country domain name for example .vn,...");
            bindingResult.addError(error);
            check = true;
        }
        if (request.getPhone().equals("")) {
            FieldError error = new FieldError("user", "phone", "Phone cannot be empty");
            bindingResult.addError(error);
            check = true;
        } else if (userRepository.existsByUserProfilePhone(request.getPhone())) {
            FieldError error = new FieldError("user", "phone", "There is already an account registered with the same phone number");
            bindingResult.addError(error);
            check = true;
        } else if (!Validate.isPhoneValid(request.getPhone())) {
            FieldError error = new FieldError("user", "phone", "Phone number must have 10 digits and start with 0");
            bindingResult.addError(error);
            check = true;
        }
        if (request.getAge().equals("")) {
            FieldError error = new FieldError("user", "age", "Age cannot be empty");
            bindingResult.addError(error);
            check = true;
        } else if (!Validate.isAgeValid(request.getAge())) {
            FieldError error = new FieldError("user", "age", "Age must be older than 16 and less than 69");
            bindingResult.addError(error);
            check = true;
        }
        if (request.getGender() == null) {
            FieldError error = new FieldError("user", "gender", "Gender cannot be empty");
            bindingResult.addError(error);
            check = true;
        }
        return check;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCaseOrUserProfileEmailIgnoreCaseOrUserProfilePhoneIgnoreCase(username, username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not Exist"));
        var role = new ArrayList<SimpleGrantedAuthority>();
        role.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), role);
    }
}
