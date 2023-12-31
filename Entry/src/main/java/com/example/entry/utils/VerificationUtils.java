package com.example.entry.utils;

import java.util.Random;

public class VerificationUtils {

    public static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            verificationCode.append(random.nextInt(10));
        }
        return String.valueOf(verificationCode);
    }

}
