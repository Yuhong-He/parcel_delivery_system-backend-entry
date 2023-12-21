package com.example.entry.utils;

import java.util.regex.Pattern;

public class UserUtils {

    public static boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return Pattern.matches(regex, email);
    }

    public static boolean validateUsername(String username) {
        return !username.isEmpty() && username.length() < 15;
    }

    public static boolean validatePassword(String pwd) {
        return pwd.length() >= 6;
    }

}
