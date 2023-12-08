package com.rushional.cities.controllers;

import com.rushional.cities.dtos.AuthenticationRequest;
import com.rushional.cities.dtos.AuthenticationResponse;
import com.rushional.cities.dtos.RefreshTokenRequest;
import com.rushional.cities.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final AuthenticationService authService;

    @PostMapping("/authenticate")
    @Operation(summary = "Authentication")
    public AuthenticationResponse authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        log.info("Log in request: customer username: {}", request.getUsername());

        return authService.authenticate(request);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token")
    public AuthenticationResponse refreshToken(
            @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh request in customer-service");

        return authService.refreshToken(request);
    }
}
