package com.miniflight.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

  private final Key key;
  private final int accessMinutes;

  public JwtService(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.access-minutes}") int accessMinutes) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessMinutes = accessMinutes;
  }

  public String createAccessToken(Long userId, String email, Set<String> roles) {
    Instant now = Instant.now();
    Instant exp = now.plus(accessMinutes, ChronoUnit.MINUTES);

    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("email", email)
        .claim("roles", roles)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(exp))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Jws<Claims> parse(String token) throws JwtException {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }
}
