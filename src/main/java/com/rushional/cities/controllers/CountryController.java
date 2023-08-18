package com.rushional.cities.controllers;

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
import java.util.Optional;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping()
    public ResponseEntity<?> getAllCountries(
            @RequestParam(value = "per_page", defaultValue = "25") int perPage,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(countryService.getAll(perPage, page), HttpStatus.OK);
    }

    @PostMapping("{id}/upload-flag")
    public ResponseEntity<?> uploadFlag(
            @PathVariable Long id,
            @RequestParam("flag_image") MultipartFile flagImage
    ) {
        return new ResponseEntity<>(countryService.uploadFlag(id, flagImage), HttpStatus.OK);
    }
}
