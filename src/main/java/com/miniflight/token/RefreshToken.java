package com.miniflight.token;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false, unique = true, length = 64)
  private String tokenHash;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(nullable = false)
  private boolean revoked;

  @Column(nullable = false)
  private Instant createdAt;

  public RefreshToken() {}

  public RefreshToken(Long userId, String tokenHash, Instant expiresAt, boolean revoked, Instant createdAt) {
    this.userId = userId;
    this.tokenHash = tokenHash;
    this.expiresAt = expiresAt;
    this.revoked = revoked;
    this.createdAt = createdAt;
  }

  public Long getId() { return id; }
  public Long getUserId() { return userId; }
  public String getTokenHash() { return tokenHash; }
  public Instant getExpiresAt() { return expiresAt; }
  public boolean isRevoked() { return revoked; }

  public void setRevoked(boolean revoked) { this.revoked = revoked; }
}
