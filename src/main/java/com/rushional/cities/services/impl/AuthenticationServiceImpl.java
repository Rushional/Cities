package com.rushional.cities.services.impl;

import com.rushional.cities.dtos.AuthenticationRequest;
import com.rushional.cities.dtos.AuthenticationResponse;
import com.rushional.cities.dtos.RefreshTokenRequest;
import com.rushional.cities.models.Customer;
import com.rushional.cities.repositories.CustomerRepository;
import com.rushional.cities.security.CustomerDetails;
import com.rushional.cities.security.JwtService;
import com.rushional.cities.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final CustomerRepository customerRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    String username = request.getUsername().toLowerCase();
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, request.getPassword()));

    Customer customer =
        customerRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("User with username '" + username + "' not found"));

    return getAuthenticationResponse(customer);
  }

  @Override
  public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
    String refreshJwt = request.getRefreshToken();
    String username = jwtService.extractUsername(refreshJwt);

    Customer customer =
        customerRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("User with username '" + username + "' not found"));

    return getAuthenticationResponse(customer);
  }

  private AuthenticationResponse getAuthenticationResponse(Customer customer) {
    return new AuthenticationResponse(
            jwtService.generateAccessJwt(new CustomerDetails(customer)),
            jwtService.generateRefreshJwt(new CustomerDetails(customer))
    );
  }
}
