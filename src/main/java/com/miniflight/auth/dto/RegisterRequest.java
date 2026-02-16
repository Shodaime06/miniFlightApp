package com.miniflight.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
  @Email @NotBlank public String email;
  @NotBlank @Size(min = 6, max = 64) public String password;
}
