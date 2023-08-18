package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CountriesResponse;
import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.exceptions.InternalServerException;
import com.rushional.cities.exceptions.NotFoundException;
import com.rushional.cities.models.CountryEntity;
import com.rushional.cities.repositories.CountryRepository;
import com.rushional.cities.services.CountryService;
import com.rushional.cities.services.PictureUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final PictureUploadService pictureUploadService;

    @Value("${constants.flags-bucket}")
    private String CITIES_BUCKET;
    @Value("${constants.flags-path-in-bucket}")
    private String FLAGS_PATH_IN_BUCKET;

    @Override
    public CountriesResponse getAll(int perPage, int page) {
        Pageable paging = PageRequest.of(page, perPage);
        Page<CountryEntity> pageCountries = countryRepository.findAll(paging);
        Page<CountryDto> pageCountryDtos = pageCountries.map(this::countryEntityToDto);
        List<CountryDto> countryDtos = pageCountryDtos.getContent();
        return new CountriesResponse(
                countryDtos, perPage, page, pageCountries.getTotalPages(), pageCountries.getTotalElements()
        );
    }

    @Override
    public CountryDto uploadFlag(Long id, MultipartFile flagImage) {
        CountryEntity country = getCountry(id);
        File tempFile;
        try {
            tempFile = File.createTempFile(country.getName() + "uuid here", null);
            tempFile.deleteOnExit();
            flagImage.transferTo(tempFile);
        } catch (IOException e) {
            throw new InternalServerException();
        }
        String extension = FilenameUtils.getExtension(flagImage.getOriginalFilename());
        String flagPath = FLAGS_PATH_IN_BUCKET +
                country.getName() + "-" + country.getUuid() + "." + extension;
        System.out.println(flagPath);
        pictureUploadService.uploadPicture(flagPath, tempFile, CITIES_BUCKET);
        tempFile.delete();
        country.setFlagPath(flagPath);
        countryRepository.save(country);
        return new CountryDto(country.getId(), country.getName(), country.getFlagPath());
    }

    private CountryDto countryEntityToDto(CountryEntity country) {
        return new CountryDto(
                country.getId(),
                country.getName(),
                country.getFlagPath());
    }

    private CountryEntity getCountry(Long id) {
        Optional<CountryEntity> countryOptional = countryRepository.findById(id);
        if (countryOptional.isEmpty()) throw new NotFoundException("Country not found");
        return countryOptional.get();
    }
}
