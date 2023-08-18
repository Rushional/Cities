package com.rushional.cities.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

  @Value("${security.jwt.issuer}")
  private String issuer;
  @Value("${security.jwt.secret-key}")
  private String secretKey;
  @Value("${security.jwt.access-token.expiration}")
  private String accessJwtExpiration;
  @Value("${security.jwt.refresh-token.expiration}")
  private String refreshJwtExpiration;
  @Value("${security.jwt.token-type}")
  private String tokenType;

  public String generateAccessJwt(UserDetails userDetails) {
    return buildToken(userDetails, Long.parseLong(accessJwtExpiration));
  }

  public String generateRefreshJwt(UserDetails userDetails) {
    return buildToken(userDetails, Long.parseLong(refreshJwtExpiration));
  }

  public boolean isTokenValid(String jwt, String username) {
    String usernameFromJwt = extractUsername(jwt);

    return (usernameFromJwt.equalsIgnoreCase(username)) && !isTokenExpired(jwt);
  }

  public String extractUsername(String jwt) {
    return extractClaim(jwt, Claims::getSubject);
  }

  private Date extractExpiration(String jwt) {
    return extractClaim(jwt, Claims::getExpiration);
  }

  public <T> T extractClaim(String jwt, Function<Claims, T> claimExtractor) {
    Claims claims = extractAllClaims(jwt);
    return claimExtractor.apply(claims);
  }

  private Claims extractAllClaims(String jwt) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(jwt)
        .getBody();
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private boolean isTokenExpired(String jwt) {
    return extractExpiration(jwt).before(new Date());
  }

  private String buildToken(UserDetails userDetails, long jwtExpiration) {
    return Jwts.builder()
        .setHeaderParam("typ", tokenType)
        .claim("customerId", ((CustomerDetails) userDetails).getCustomerEntity().getId())
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .setIssuer(issuer)
        .claim("roles", userDetails.getAuthorities())
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }
}
