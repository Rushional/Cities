package com.rushional.cities.exception_handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rushional.cities.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthenticationErrorHandler {

  private static final String CONTENT_TYPE = "application/json";
  private static final String AUTH_ERROR_MESSAGE =
      "Authentication required. Please provide a valid token";

  public void handleAuthenticationError(HttpServletResponse response, Exception ex)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(CONTENT_TYPE);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .error(ex.getClass().getSimpleName())
        .status(HttpStatus.UNAUTHORIZED.value())
        .message(AUTH_ERROR_MESSAGE)
        .build();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
