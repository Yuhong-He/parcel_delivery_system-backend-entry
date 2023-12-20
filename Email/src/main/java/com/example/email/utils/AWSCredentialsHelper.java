package com.example.email.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

import java.util.Properties;

public class AWSCredentialsHelper {
    public static AWSCredentials getAwsCredentials() {
        Properties props = SecurityKeyHelper.getProperties();
        return new BasicAWSCredentials(props.getProperty("aws.accessKey"), props.getProperty("aws.secretKey"));
    }
}
