package com.rushional.cities.configs;

import com.rushional.cities.exception_handling.AuthenticationErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationFilter authFilter;
  private final AuthenticationProvider authenticationProvider;
  private final AuthenticationErrorHandler errorHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/countries/{id}/upload-flag")
        .hasRole("EDITOR")
        .requestMatchers(HttpMethod.PATCH, "/cities/{id}")
        .hasRole("EDITOR")
        .anyRequest()
        .permitAll()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(customAuthenticationEntryPoint());

    return http.build();
  }

  private AuthenticationEntryPoint customAuthenticationEntryPoint() {
    return (request, response, authException) -> errorHandler.handleAuthenticationError(response,
        authException);
  }
}
