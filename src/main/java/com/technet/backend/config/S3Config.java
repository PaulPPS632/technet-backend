package com.technet.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.acceskeyId}")
    private String accesKey;

    @Value("${aws.secretke}")
    private String privatekey;
    @Value("${aws.region}")
    private String region;
    @Bean
    public S3Client s3Client(){
        //Region region = Region.US_East_2;
        //AwsCredentials credentials = new BasicAWSCredentials(accesKey, privatekey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accesKey, privatekey)))
                .build();
    }

}
