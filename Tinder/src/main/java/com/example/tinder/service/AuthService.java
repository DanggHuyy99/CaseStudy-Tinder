package com.example.tinder.service;

import com.example.tinder.model.User;
import com.example.tinder.model.UserProfile;
import com.example.tinder.model.role.Role;
import com.example.tinder.repository.UserProfileRepository;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.auth.request.RegisterRequest;
import com.example.tinder.ulti.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passworEncoder;

    private final UserProfileRepository userProfileRepository;

    public void register(RegisterRequest request) {
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

        userProfile.setUser(userRepository.save(user)); // Thiết lập liên kết ngược với User và lưu UserProfile
        userProfileRepository.save(userProfile);
    }

    public boolean checkUsernameOrPhoneNumberOrEmail(RegisterRequest request, BindingResult bindingResult) {
        boolean check = false;
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            FieldError error = new FieldError("user", "username", "There is already an account registered with the same username");
            bindingResult.addError(error);

//            bindingResult.reject("username", null,
//                    "There is already an account registered with the same username");
            check = true;
        }
        if (userRepository.existsByUserProfileEmail(request.getEmail())) {
            FieldError error = new FieldError("user", "email", "There is already an account registered with the same email");
            bindingResult.addError(error);

            bindingResult.reject("email", null,
                    "There is already an account registered with the same email");
            check = true;
        }
        if (userRepository.existsByUserProfilePhone(request.getPhone())) {
            FieldError error = new FieldError("user", "phoneNumber", "There is already an account registered with the same phone number");
            bindingResult.addError(error);

            bindingResult.reject("phoneNumber", null,
                    "There is already an account registered with the same phone number");
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
