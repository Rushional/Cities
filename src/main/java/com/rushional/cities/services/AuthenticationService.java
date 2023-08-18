package com.rushional.cities.services;

import com.rushional.cities.dtos.AuthenticationRequest;
import com.rushional.cities.dtos.AuthenticationResponse;
import com.rushional.cities.dtos.RefreshTokenRequest;

public interface AuthenticationService {

  AuthenticationResponse authenticate(AuthenticationRequest request);

  AuthenticationResponse refreshToken(RefreshTokenRequest request);
}
