package org.example.receiver.utils;

import org.example.receiver.dto.Email;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Properties;

public class EmailEncryptor {

    static final String ALGORITHM = "AES";

    public static byte[] encrypt(Email email) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        Key key = new SecretKeySpec(getKey(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(email.toString().getBytes());
    }

    private static byte[] getKey() throws IOException {
        Properties props = new Properties();
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(EmailEncryptor.class.getClassLoader().getResourceAsStream("securityKey.properties")),
                StandardCharsets.UTF_8);
        props.load(inputStreamReader);
        return props.getProperty("emailKey").getBytes();
    }
}
