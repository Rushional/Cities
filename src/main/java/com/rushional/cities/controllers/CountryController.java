package com.rushional.cities.controllers;

import com.rushional.cities.services.CountryService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping()
    public ResponseEntity<?> getAllCountries(
            @RequestParam(value = "per_page", defaultValue = "25") @NotBlank int perPage,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(countryService.getAll(perPage, page), HttpStatus.OK);
    }

    @PostMapping("{countryId}/upload-flag")
    public ResponseEntity<?> uploadFlag(
            @PathVariable Long countryId,
            @RequestParam("flag_image") MultipartFile flagImage
    ) {
        return new ResponseEntity<>(countryService.uploadFlag(countryId, flagImage), HttpStatus.OK);
    }
}
