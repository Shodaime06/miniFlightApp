package com.miniflight.auth;

import com.miniflight.user.User;
import com.miniflight.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {

  private final UserRepository users;

  public MeController(UserRepository users) {
    this.users = users;
  }

  @GetMapping("/api/me")
  public Object me(Authentication auth) {
    if (auth == null) return null;
    Long userId = Long.valueOf(String.valueOf(auth.getPrincipal()));
    User u = users.findById(userId).orElseThrow();
    return Map.of(
        "id", u.getId(),
        "email", u.getEmail(),
        "roles", u.getRoles()
    );
  }
}
