package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CountriesResponse;
import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.exceptions.NotFoundException;
import com.rushional.cities.models.Country;
import com.rushional.cities.repositories.CountryRepository;
import com.rushional.cities.services.CountryService;
import com.rushional.cities.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public CountriesResponse getAll(int perPage, int page) {
        Pageable paging = PageRequest.of(page, perPage);
        Page<Country> pageCountries = countryRepository.findAll(paging);
        Page<CountryDto> pageCountryDtos = pageCountries.map(this::countryEntityToDto);
        List<CountryDto> countryDtos = pageCountryDtos.getContent();
        return new CountriesResponse(
                countryDtos, perPage, page, pageCountries.getTotalPages(), pageCountries.getTotalElements()
        );
    }

    @Override
    public Country getCountryById(Long id) {
        return countryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Country not found")
        );
    }

    private CountryDto countryEntityToDto(Country country) {
        return new CountryDto(
                country.getId(),
                country.getName());
    }
}
