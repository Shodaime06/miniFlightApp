package com.miniflight.auth;

import com.miniflight.security.JwtService;
import com.miniflight.token.RefreshTokenService;
import com.miniflight.user.User;
import com.miniflight.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;
  private final RefreshTokenService refresh;

  public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt, RefreshTokenService refresh) {
    this.users = users;
    this.encoder = encoder;
    this.jwt = jwt;
    this.refresh = refresh;
  }

  public void register(String email, String password) {
    if (users.existsByEmail(email)) throw new RuntimeException("Email already used");
    users.save(new User(email, encoder.encode(password), Set.of("USER")));
  }

  public LoginResult login(String email, String password) {
    User u = users.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid credentials"));
    if (!encoder.matches(password, u.getPasswordHash())) throw new RuntimeException("Invalid credentials");

    String access = jwt.createAccessToken(u.getId(), u.getEmail(), u.getRoles());
    String refreshToken = refresh.issueAndStore(u.getId());
    return new LoginResult(access, refreshToken);
  }

  public RefreshResult refresh(String rawRefreshToken) {
    Long userId = refresh.validateAndGetUserId(rawRefreshToken);
    User u = users.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    String newAccess = jwt.createAccessToken(u.getId(), u.getEmail(), u.getRoles());
    String newRefresh = refresh.rotate(u.getId(), rawRefreshToken);
    return new RefreshResult(newAccess, newRefresh);
  }

  public void logout(String rawRefreshToken) {
    if (rawRefreshToken != null && !rawRefreshToken.isBlank()) refresh.revoke(rawRefreshToken);
  }

  public record LoginResult(String accessToken, String refreshToken) {}
  public record RefreshResult(String accessToken, String refreshToken) {}
}
