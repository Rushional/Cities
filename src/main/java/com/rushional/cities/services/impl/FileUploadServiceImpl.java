package com.rushional.cities.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.AwsHostNameUtils;
import com.rushional.cities.exceptions.InternalServerException;
import com.rushional.cities.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${constants.image-host}")
    private String IMAGE_HOST;

    @Override
    public String uploadFile(String pathToFile, String fileNameWithoutExtension, File file, String bucketName) {
        File tempFile;
        try {
            tempFile = File.createTempFile(fileNameWithoutExtension, null);
            tempFile.deleteOnExit();
            FileChannel src = new FileInputStream(file).getChannel();
            FileChannel dest = new FileOutputStream(tempFile).getChannel();
            dest.transferFrom(src, 0, src.size());
        } catch (IOException e) {
            throw new InternalServerException("File creation failure");
        }
        String extension = FilenameUtils.getExtension(file.getName());
        String fullFilePath = pathToFile + fileNameWithoutExtension + extension;
        upload(fullFilePath, tempFile, bucketName);
        tempFile.delete();
        return fullFilePath;
    }

    @Override
    public String uploadMultipartFile(String pathToFile, String fileNameWithoutExtension,
                                    MultipartFile multipartFile, String bucketName) {
        File tempFile;
        try {
            tempFile = File.createTempFile(fileNameWithoutExtension, null);
            tempFile.deleteOnExit();
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            throw new InternalServerException("File creation failure");
        }
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String fullFilePath = pathToFile + fileNameWithoutExtension + extension;
        upload(fullFilePath, tempFile, bucketName);
        tempFile.delete();
        return fullFilePath;
    }

    private void upload(String fullFilePath, File file, String bucketName) {
        AmazonS3 client = getS3Client();
        createS3Bucket(client, bucketName);
        try {
            client.putObject(bucketName, fullFilePath, file);
        } catch (AmazonServiceException e) {
            throw new InternalServerException("Image upload failed");
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
        setPublicReadOnly(client, bucketName);
    }

    private void setPublicReadOnly(AmazonS3 client, String bucketName) {
        Policy bucketPolicy = new Policy().withStatements(
                new Statement(Statement.Effect.Allow)
                        .withPrincipals(Principal.AllUsers)
                        .withActions(S3Actions.GetObject)
                        .withResources(new Resource(
                                "arn:aws:s3:::" + bucketName + "/*")));
        client.setBucketPolicy(new SetBucketPolicyRequest(bucketName, bucketPolicy.toJson()));
    }


}
