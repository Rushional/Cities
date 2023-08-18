package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CountriesResponse;
import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.exceptions.InternalServerException;
import com.rushional.cities.exceptions.NotFoundException;
import com.rushional.cities.models.CountryEntity;
import com.rushional.cities.repositories.CountryRepository;
import com.rushional.cities.services.CountryService;
import com.rushional.cities.services.FileUploadService;
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
    private final FileUploadService fileUploadService;

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
    public CountryDto uploadFlag(Long countryId, MultipartFile flagImage) {
        CountryEntity country = getCountryById(countryId);
        String fullFilePath = fileUploadService.uploadMultipartFile(
                FLAGS_PATH_IN_BUCKET,
                getFileNameWithoutExtension(country),
                flagImage,
                CITIES_BUCKET
        );
        country.setFlagPath(fullFilePath);
        countryRepository.save(country);
        return new CountryDto(country.getId(), country.getName(), country.getFlagPath());
    }

    @Override
    public CountryEntity getCountryById(Long id) {
        Optional<CountryEntity> countryOptional = countryRepository.findById(id);
        if (countryOptional.isEmpty()) throw new NotFoundException("Country not found");
        return countryOptional.get();
    }

    public String getFileNameWithoutExtension(CountryEntity country) {
        return country.getName() + "-" + country.getUuid() + ".";
    }

    private CountryDto countryEntityToDto(CountryEntity country) {
        return new CountryDto(
                country.getId(),
                country.getName(),
                country.getFlagPath());
    }
}
