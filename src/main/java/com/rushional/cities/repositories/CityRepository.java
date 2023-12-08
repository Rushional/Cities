package com.rushional.cities.repositories;

import com.rushional.cities.models.City;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

  Page<City> findByName(String name, Pageable pageable);

  @Query("SELECT city FROM city city JOIN FETCH city.country country " +
          "WHERE country.name = :countryName")
  Page<City> findByCountryName(@Param("countryName") String countryName, Pageable pageable);

  @Query("SELECT city FROM city city JOIN FETCH city.country country " +
          "WHERE city.name = :cityName AND country.name = :countryName")
  Page<City> findByCityNameAndCountryName(
          @Param("cityName") String cityName,
          @Param("countryName") String countryName,
          Pageable pageable
  );

  @Query("SELECT DISTINCT c.name FROM city c " +
         "ORDER BY c.name ASC")
  List<String> findDistinctNames();
}
