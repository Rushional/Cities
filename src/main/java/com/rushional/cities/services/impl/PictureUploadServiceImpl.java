package com.rushional.cities.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
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
    @Value("${constants.flags-bucket}")
    private String FLAGS_BUCKET;

    @Override
    public void uploadPicture(String picturePath, File file) {
        String endpoint = IMAGE_HOST;
        var client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                endpoint,
                                AwsHostNameUtils.parseRegion(endpoint, AmazonS3Client.S3_SERVICE_NAME)
                        )
                )
                .build();

        List<Bucket> buckets = client.listBuckets();
        System.out.println("Your Amazon S3 buckets are:");
        for (Bucket b : buckets) {
            System.out.println("* " + b.getName());
        }

        try {
            client.putObject(FLAGS_BUCKET, picturePath, file);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }
}
