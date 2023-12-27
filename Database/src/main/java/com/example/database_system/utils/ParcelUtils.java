package com.example.database_system.utils;

import com.example.database_system.dto.CreateParcelData;

import java.util.List;

public class ParcelUtils {

    static List<Integer> parcel_type = List.of(1, 2, 3);
    static List<String> ucd_residence = List.of("Ashfield", "Belgrove", "Blackrock",
            "Glenomena", "Merville", "Proby", "Roebuck Hall", "Roebuck Castle", "Village");

    public static boolean validateParcelCreate(CreateParcelData data) {
        return parcel_type.contains(data.getType())
                && ucd_residence.contains(data.getAddress1())
                && !data.getAddress2().isEmpty();
    }
}
