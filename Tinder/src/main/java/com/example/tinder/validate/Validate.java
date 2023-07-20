package com.example.tinder.validate;

import java.util.regex.Pattern;

public class Validate {
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{1,15}$";
    public static boolean isUsernameValid(String username) {
        return Pattern.compile(USERNAME_REGEX).matcher(username).matches();
    }
    public static final String PASSWORD_REGEX = "^[a-zA-Z0-9]{1,15}$";
    public static boolean isPasswordValid(String password) {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }
    public static final String NAME_REGEX = "^[a-zA-ZÀ-Ỹà-ỹ]+( [a-zA-ZÀ-Ỹà-ỹ]+)*$";
    public static boolean isFullNameValid(String name) {
        return Pattern.compile(NAME_REGEX).matcher(name).matches();
    }
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|edu|net|org|biz|info|vn|pro|[A-Za-z]{2})$";
    public static boolean isEmailValid(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }
    public static final String PHONE_REGEX = "^[0][0-9]{9}$";
    public static boolean isPhoneValid(String phone) {
        return Pattern.compile(PHONE_REGEX).matcher(phone).matches();
    }
    public static boolean isAgeValid(String ageS) {
        try {
            int age = Integer.parseInt(ageS);
            if (age >= 16 && age <= 69)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
