package com.rushional.cities.jobs;

import com.rushional.cities.dtos.CountryDto;
import com.rushional.cities.exceptions.InternalServerException;
import com.rushional.cities.models.CountryEntity;
import com.rushional.cities.repositories.CountryRepository;
import com.rushional.cities.services.CountryService;
import com.rushional.cities.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@Component
@RequiredArgsConstructor
public class OnStartupJob implements InitializingBean {
    private final CountryService countryService;
    private final FileUploadService fileUploadService;
    private final CountryRepository countryRepository;

    @Value("${constants.flags-bucket}")
    private String CITIES_BUCKET;
    @Value("${constants.flags-path-in-bucket}")
    private String FLAGS_PATH_IN_BUCKET;

    @Override
    public void afterPropertiesSet() throws Exception {
        uploadFlag("src/main/resources/images/georgia-flag.jpg", 1);
        uploadFlag("src/main/resources/images/Flag_of_China.png", 2);
        uploadFlag("src/main/resources/images/India-flag.jpg", 3);
        uploadFlag("src/main/resources/images/USA-flag.png", 4);
        uploadFlag("src/main/resources/images/Russia-flag.jpg", 5);
    }

    private void uploadFlag(String pathToFlagImage, long countryId) {
        CountryEntity country = countryService.getCountryById(countryId);
        String fullFilePath = fileUploadService.uploadFile(
                FLAGS_PATH_IN_BUCKET,
                countryService.getFileNameWithoutExtension(country),
                new File(pathToFlagImage),
                CITIES_BUCKET
        );
        country.setFlagPath(fullFilePath);
        countryRepository.save(country);
    }
}
