package com.miniflight.auth.dto;

public class AuthResponse {
  public String accessToken;
  public AuthResponse(String accessToken) { this.accessToken = accessToken; }
}
