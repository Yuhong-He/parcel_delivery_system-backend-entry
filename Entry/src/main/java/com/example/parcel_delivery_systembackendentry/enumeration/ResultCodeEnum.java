package com.example.parcel_delivery_systembackendentry.enumeration;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    // user related
    SUCCESS(200,"Success"),
    FAIL(201, "Fail"),
    NOT_REGISTERED_API(202, "Not Registered API"),
    NO_PERMISSION(203, "No Permission"),
    NO_USER(205, "User Not Exist"),
    PASSWORD_INCORRECT(206, "Incorrect Password"),
    PASSWORD_NOT_MATCH(207, "Password not match"),
    EMAIL_NOT_REGISTERED(208, "Email Not Registered"),
    INVALID_EMAIL(210, "Invalid Email Address"),
    LESS_THAN_ONE_MINUTE(211, "Please wait one minute"),
    VERIFY_CODE_EXPIRED(213, "Verification code expired"),
    VERIFY_CODE_INCORRECT(214, "Verification code incorrect"),
    EMAIL_ALREADY_REGISTERED(215, "Email already registered"),
    USERNAME_LENGTH(216, "Username length incorrect"),
    PASSWORD_LENGTH(217, "Password length incorrect"),
    NO_VERIFICATION_CODE(218, "Verification code does not send before"),
    TOKEN_EXPIRED(223,"Token Expired"),
    TOKEN_ERROR(224,"Invalid Token"),
    MISS_TOKEN(225,"Bad Request: Token Missing"),
    EMPTY_SEARCH(226, "Empty Search"),
    STUDENT_NOT_EXIST(227, "Student Not Exist"),

    // parcel related
    ParcelInfoIncomplete(301, "Parcel Info Incomplete");

    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
