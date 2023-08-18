package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.AuthenticationRequest;
import com.rushional.cities.dtos.AuthenticationResponse;
import com.rushional.cities.dtos.RefreshTokenRequest;
import com.rushional.cities.models.CustomerEntity;
import com.rushional.cities.repositories.CustomerRepository;
import com.rushional.cities.security.CustomerDetails;
import com.rushional.cities.security.JwtService;
import com.rushional.cities.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final CustomerRepository customerRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    String email = request.getUsername().toLowerCase();
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, request.getPassword()));

    CustomerEntity customer =
        customerRepository.findByUsername(email).orElseThrow(
            () -> new UsernameNotFoundException("User with email '" + email + "' not found"));

    return getAuthenticationResponse(customer);
  }

  @Override
  public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
    String refreshJwt = request.getRefreshToken();
    String email = jwtService.extractUsername(refreshJwt);

    CustomerEntity customer =
        customerRepository.findByUsername(email).orElseThrow(
            () -> new UsernameNotFoundException("User with email '" + email + "' not found"));

    return getAuthenticationResponse(customer);
  }

  private AuthenticationResponse getAuthenticationResponse(CustomerEntity customer) {
    return new AuthenticationResponse(
            jwtService.generateAccessJwt(new CustomerDetails(customer)),
            jwtService.generateRefreshJwt(new CustomerDetails(customer))
    );
  }
}
