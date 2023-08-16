package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CityDto;
import com.rushional.cities.services.CityService;
import com.rushional.cities.services.PictureUploadService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping()
    public List<CityDto> getUniqueCountries() {
        return cityService.getAllCities();
    }

    @PostMapping("/upload-file")
    public void handleFileUpload(@RequestParam("inputFile") MultipartFile inputFile) throws IOException {
        File tempFile = File.createTempFile("spain-", null);
        tempFile.deleteOnExit();
        inputFile.transferTo(tempFile);
        pictureUploadService.uploadPicture("cabbage.jpg", tempFile);
        tempFile.delete();
    }
}
