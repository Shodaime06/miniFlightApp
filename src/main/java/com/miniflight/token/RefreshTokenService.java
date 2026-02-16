package com.miniflight.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenService {

  private final RefreshTokenRepository repo;
  private final int refreshDays;

  public RefreshTokenService(RefreshTokenRepository repo,
                             @Value("${app.jwt.refresh-days}") int refreshDays) {
    this.repo = repo;
    this.refreshDays = refreshDays;
  }

  public String issueAndStore(Long userId) {
    String raw = UUID.randomUUID() + "." + UUID.randomUUID();
    String hash = sha256Hex(raw);

    Instant now = Instant.now();
    Instant exp = now.plus(refreshDays, ChronoUnit.DAYS);

    repo.save(new RefreshToken(userId, hash, exp, false, now));
    return raw;
  }

  public Long validateAndGetUserId(String rawToken) {
    String hash = sha256Hex(rawToken);
    RefreshToken rt = repo.findByTokenHash(hash).orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    if (rt.isRevoked()) throw new RuntimeException("Refresh token revoked");
    if (rt.getExpiresAt().isBefore(Instant.now())) throw new RuntimeException("Refresh token expired");

    return rt.getUserId();
  }

  public void revoke(String rawToken) {
    String hash = sha256Hex(rawToken);
    repo.findByTokenHash(hash).ifPresent(rt -> {
      rt.setRevoked(true);
      repo.save(rt);
    });
  }

  public String rotate(Long userId, String oldRaw) {
    revoke(oldRaw);
    return issueAndStore(userId);
  }

  private String sha256Hex(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] dig = md.digest(input.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(dig);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
