package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.services.CityService;
import com.rushional.cities.services.PictureUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;
    private final PictureUploadService pictureUploadService;

    @Value("${constants.flags-bucket}")
    private String CITIES_BUCKET;
    @Value("${constants.flags-path-in-bucket}")
    private String FLAGS_PATH_IN_BUCKET;

    @GetMapping()
    public List<CityDto> getUniqueCountries() {
        return cityService.getAllCities();
    }

    @PostMapping("/upload-file")
    public void handleFileUpload(@RequestParam("inputFile") MultipartFile inputFile) throws IOException {
        File tempFile = File.createTempFile("spain-", null);
        tempFile.deleteOnExit();
        inputFile.transferTo(tempFile);
        pictureUploadService.uploadPicture(FLAGS_PATH_IN_BUCKET + "cabbage.jpg", tempFile, CITIES_BUCKET);
        tempFile.delete();
    }
}
