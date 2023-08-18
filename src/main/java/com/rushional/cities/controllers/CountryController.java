package com.rushional.cities.controllers;

import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.exceptions.NotFoundException;
import com.rushional.cities.models.CountryEntity;
import com.rushional.cities.repositories.CountryRepository;
import com.rushional.cities.services.CountryService;
import com.rushional.cities.services.PictureUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;
    private final CountryRepository countryRepository;
    private final PictureUploadService pictureUploadService;

    @Value("${constants.flags-bucket}")
    private String CITIES_BUCKET;
    @Value("${constants.flags-path-in-bucket}")
    private String FLAGS_PATH_IN_BUCKET;

    @GetMapping()
    public ResponseEntity<?> getAllCountries(
            @RequestParam(value = "per_page", defaultValue = "25") int perPage,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(countryService.getAll(perPage, page), HttpStatus.OK);
    }

    @PostMapping("{id}/upload-flag")
    public ResponseEntity<?> handleFileUpload(
            @PathVariable Long id,
            @RequestParam("flagImage") MultipartFile flagImage
    ) throws IOException {
        CountryEntity country = getCountry(id);
        File tempFile = File.createTempFile(country.getName() + "uuid here", null);
        tempFile.deleteOnExit();
        flagImage.transferTo(tempFile);
        String flagPath = FLAGS_PATH_IN_BUCKET + "cabbage.jpg";
        pictureUploadService.uploadPicture(flagPath, tempFile, CITIES_BUCKET);
        tempFile.delete();
        country.setFlagPath(flagPath);
        countryRepository.save(country);
        return ResponseEntity.ok().build();
    }

    private CountryEntity getCountry(Long id) {
        Optional<CountryEntity> countryOptional = countryRepository.findById(id);
        if (countryOptional.isEmpty()) throw new NotFoundException("Country not found");
        return countryOptional.get();
    }
}
