package com.example.parcel_delivery_systembackendentry.utils;

public class PermissionHelper {

    public static String[] unregistered() {
        return new String[]{
                "/user/login",
                "/user/sendRegisterEmail",
                "/user/register",
                "/api-docs",
                "/swagger-ui/*"
        };
    }

    public static String[] anyRegistered() {
        return new String[]{
                "/parcel/list"
        };
    }

    public static String[] student() {
        return new String[]{

        };
    }

    public static String[] postman() {
        return new String[]{

        };
    }

    public static String[] mervilleStaff() {
        return new String[]{

        };
    }

    public static String[] estateServiceStaff() {
        return new String[]{
                "/user/searchStudentByName",
                "/parcel/create"
        };
    }

}
