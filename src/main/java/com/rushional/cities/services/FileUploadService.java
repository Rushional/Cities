package com.rushional.cities.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileUploadService {
    String uploadFile(String pathToFile, String fileNameWithoutExtension,
                           File file, String bucketName);

    String uploadMultipartFile(String pathToFile, String fileNameWithoutExtension,
                                    MultipartFile multipartFile, String bucketName);
}
