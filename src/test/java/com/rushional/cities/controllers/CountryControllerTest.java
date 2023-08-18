package com.rushional.cities.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebTestClient
public class CountryControllerTest extends AbstractTest {
    private static final String COUNTRIES_ENDPOINT = "/countries";

    @Test
    void succeedsCountries() throws Exception {
        String contentAsString = mockMvc.perform(get(COUNTRIES_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson = objectMapper.readTree(contentAsString);
        assertEquals("Georgia", responseJson.get("countries").get(0).get("name").asText());
    }
}
