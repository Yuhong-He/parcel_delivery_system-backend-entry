package com.example.parcel_delivery_systembackendentry.utils;

public class PermissionHelper {

    public static String[] unregistered() {
        return new String[]{
                "/api/user/login",
                "/api/user/sendRegisterEmail",
                "/api/user/register"
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

    public static String[] mervillStaff() {
        return new String[]{

        };
    }

    public static String[] estateServiceStaff() {
        return new String[]{

        };
    }

}
