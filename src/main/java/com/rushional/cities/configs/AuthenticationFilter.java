package com.rushional.cities.configs;

import com.rushional.cities.exception_handling.AuthenticationErrorHandler;
import com.rushional.cities.security.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTH_HEADER_NAME = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";
  private static final int TOKEN_EXTRACT_OFFSET = 7;

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final AuthenticationErrorHandler errorHandler;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader(AUTH_HEADER_NAME);

    if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String jwt = authHeader.substring(TOKEN_EXTRACT_OFFSET);
      String customerEmail = jwtService.extractUsername(jwt);

      if (customerEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails =
            userDetailsService.loadUserByUsername(customerEmail.toLowerCase());
        if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(userDetails, null,
                  userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (JwtException ex) {
      errorHandler.handleAuthenticationError(response, ex);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
