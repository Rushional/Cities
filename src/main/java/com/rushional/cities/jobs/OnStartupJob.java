package com.rushional.cities.jobs;

import com.rushional.cities.models.City;
import com.rushional.cities.repositories.CityRepository;
import com.rushional.cities.services.CityService;
import com.rushional.cities.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class OnStartupJob implements InitializingBean {
    private final CityService cityService;
    private final FileUploadService fileUploadService;
    private final CityRepository cityRepository;

    @Value("${constants.cities-bucket}")
    private String CITIES_BUCKET;
    @Value("${constants.logos-path-in-bucket}")
    private String LOGOS_PATH_IN_BUCKET;

    @Override
    public void afterPropertiesSet() throws Exception {
        uploadLogo("src/main/resources/images/georgia-flag.jpg", 1);
        uploadLogo("src/main/resources/images/Flag_of_China.png", 5);
        uploadLogo("src/main/resources/images/India-flag.jpg", 9);
        uploadLogo("src/main/resources/images/USA-flag.png", 11);
        uploadLogo("src/main/resources/images/Russia-flag.jpg", 15);
    }

        private void uploadLogo(String pathToFlagImage, long cityId) {
        City city = cityService.getCityById(cityId);
        String fullFilePath = fileUploadService.uploadFile(
                LOGOS_PATH_IN_BUCKET,
                cityService.getFileNameWithoutExtension(city),
                new File(pathToFlagImage),
                CITIES_BUCKET
        );
        city.setLogoPath(fullFilePath);
        cityRepository.save(city);
    }
}
