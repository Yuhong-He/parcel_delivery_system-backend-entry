package com.example.parcel_delivery_systembackendentry.utils;

public class EmailContentHelper {

    public static String getRegisterVerificationEmailBody(String code) {
        return "<p>Welcome, this is your verification code: </p>" +
                "<p style='text-align:center; font-weight: bold;'>" + code + "</p>" +
                "<p>The verification code has 5 minutes expiration. Please verify as soon as possible.</p>" +
                errorEmail() +
                signature();
    }

    private static String errorEmail() {
        return "<p>If this is not your email, please ignore the message.</p>";
    }

    private static String signature() {
        return  "<hr>" +
                "<p>UCD Parcel Service</p>";
    }

}
