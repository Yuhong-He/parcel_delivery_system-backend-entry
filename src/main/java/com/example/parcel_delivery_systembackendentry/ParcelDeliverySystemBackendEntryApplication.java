package com.example.parcel_delivery_systembackendentry;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SecurityScheme(name = "Accesstoken", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
@SpringBootApplication
@ServletComponentScan
public class ParcelDeliverySystemBackendEntryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParcelDeliverySystemBackendEntryApplication.class, args);
    }

}
