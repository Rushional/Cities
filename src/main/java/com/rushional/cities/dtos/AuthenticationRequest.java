package com.rushional.cities.dtos;

import com.rushional.cities.validation.CustomPassword;
import com.rushional.cities.validation.CustomUsername;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  @CustomUsername
  private String username;
  @CustomPassword
  private String password;
}
