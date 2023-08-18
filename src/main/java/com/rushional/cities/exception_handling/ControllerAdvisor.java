package com.rushional.cities.exception_handling;

import com.rushional.cities.dtos.ErrorResponse;
import com.rushional.cities.exceptions.InternalServerException;
import com.rushional.cities.exceptions.NotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerAdvisor {

  @ExceptionHandler({AuthenticationException.class, JwtException.class})
  private ResponseEntity<ErrorResponse> getErrorResponseEntity(Exception ex) {
    return createErrorResponse(HttpStatus.UNAUTHORIZED, ex, ex.getMessage());
  }

  @ExceptionHandler({UsernameNotFoundException.class, NotFoundException.class})
  private ResponseEntity<ErrorResponse> getErrorResponseEntity(UsernameNotFoundException ex) {
    return createErrorResponse(HttpStatus.NOT_FOUND, ex, ex.getMessage());
  }

  @ExceptionHandler(InternalServerException.class)
  private ResponseEntity<ErrorResponse> getErrorResponseEntity(InternalServerException ex) {
    return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getMessage());
  }

  private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, Exception ex,
      String message) {

    return new ResponseEntity<>(ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .error(ex.getClass().getSimpleName())
        .status(status.value())
        .message(message)
        .build(),
        status);
  }
}
