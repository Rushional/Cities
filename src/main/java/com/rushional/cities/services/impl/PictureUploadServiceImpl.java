package com.rushional.cities.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.util.AwsHostNameUtils;
import com.rushional.cities.services.PictureUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PictureUploadServiceImpl implements PictureUploadService {

    @Value("${constants.image-host}")
    private String IMAGE_HOST;

    @Override
    public void uploadPicture(String picturePath, File file, String bucketName) {
        AmazonS3 client = getS3Client();
        createS3Bucket(client, bucketName);
        try {
            client.putObject(bucketName, picturePath, file);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }

    private AmazonS3 getS3Client() {
        String endpoint = IMAGE_HOST;
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                endpoint,
                                AwsHostNameUtils.parseRegion(endpoint, AmazonS3Client.S3_SERVICE_NAME)
                        )
                )
                .build();
    }

    private void createS3Bucket(AmazonS3 client, String bucketName) {
        if(client.doesBucketExist(bucketName)) {
            return;
        }
        client.createBucket(bucketName);
    }
}
