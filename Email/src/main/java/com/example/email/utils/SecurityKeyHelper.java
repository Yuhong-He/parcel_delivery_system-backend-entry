package com.example.email.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class SecurityKeyHelper {
    public static Properties getProperties() {
        Properties props = new Properties();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    Objects.requireNonNull(EmailDecryptor.class.getClassLoader().getResourceAsStream("securityKey.properties")),
                    StandardCharsets.UTF_8);
            props.load(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }
}
