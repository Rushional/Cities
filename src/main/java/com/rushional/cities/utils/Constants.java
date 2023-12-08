package com.rushional.cities.utils;

public class Constants {
    public static final int PER_PAGE_NOT_PAGINATED = -1;
    public static final int PAGE_SIZE_NOT_PAGINATED = Integer.MAX_VALUE;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int MAX_CITY_NAME_SIZE = 100;
    public static final int MAX_COUNTRY_NAME_SIZE = 100;
    public static final String PER_PAGE_VALIDATION_ERROR_MESSAGE = "per_page should be either -1 or a positive integer";
    public static final String PAGE_NUMBER_VALIDATION_ERROR_MESSAGE = "page number should be a non-negative integer";
    public static final String CITY_NAME_VALIDATION_ERROR_MESSAGE = "city_name can't exceed " + MAX_CITY_NAME_SIZE + " symbols";
    public static final String COUNTRY_NAME_VALIDATION_ERROR_MESSAGE = "country_name can't exceed " + MAX_COUNTRY_NAME_SIZE + " symbols";
    public static final int MIN_USERNAME_LENGTH = 8;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final String USERNAME_VALIDATION_ERROR_MESSAGE = "username should be " + MIN_USERNAME_LENGTH + " to " + MAX_USERNAME_LENGTH + " characters long";
}
