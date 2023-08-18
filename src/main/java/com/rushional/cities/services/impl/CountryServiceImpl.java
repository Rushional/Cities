package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.CountriesResponse;
import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.models.CountryEntity;
import com.rushional.cities.repositories.CountryRepository;
import com.rushional.cities.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

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

    private CountryDto countryEntityToDto(CountryEntity country) {
        return new CountryDto(
                country.getId(),
                country.getName(),
                country.getFlagPath());
    }
}
