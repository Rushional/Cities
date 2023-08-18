package com.rushional.cities.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebTestClient
public class CityControllerTest extends AbstractTest {
    private static final String CITIES_ENDPOINT = "/cities";

    @Test
    void succeedsCities() throws Exception {
        String contentAsString = mockMvc.perform(get(CITIES_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson = objectMapper.readTree(contentAsString);
        assertEquals("Tbilisi", responseJson.get("cities").get(0).get("city_name").asText());
        assertEquals("Georgia", responseJson.get("cities").get(0).get("country_name").asText());
        String flagPath = responseJson.get("cities").get(0).get("flag_path").asText();
        assertTrue(flagPath.contains("Georgia"));
    }
}
