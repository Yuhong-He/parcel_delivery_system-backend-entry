package com.example.parcel_delivery_systembackendentry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ParcelDeliverySystemBackendEntryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParcelDeliverySystemBackendEntryApplication.class, args);
    }

}
