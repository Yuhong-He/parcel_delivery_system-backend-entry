package com.example.email.utils;

import com.example.email.dto.Email;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class EmailDecryptor {
    private static final String ALGORITHM = "AES";

    public static Email decrypt(byte[] encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        Key key = new SecretKeySpec(getKey(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        String decryptedString = new String(decryptedBytes);
        String[] parts = decryptedString.split(";;");
        String to = parts[0];
        String subject = parts[1];
        String htmlBody = parts[2];
        return new Email(to, subject, htmlBody);
    }

    private static byte[] getKey() {
        Properties props = SecurityKeyHelper.getProperties();
        return props.getProperty("emailKey").getBytes();
    }
}
