package com.miniflight.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

  private final UserRepository users;
  private final PasswordEncoder encoder;

  public UserAdminController(UserRepository users, PasswordEncoder encoder) {
    this.users = users;
    this.encoder = encoder;
  }

  @GetMapping
  public List<User> all() {
    return users.findAll();
  }

  public static class CreateUserDto {
    @Email @NotBlank public String email;
    @NotBlank @Size(min=6, max=64) public String password;
    public String role; // USER or ADMIN
  }

  @PostMapping
  public User create(@RequestBody CreateUserDto dto) {
    String role = (dto.role == null || dto.role.isBlank()) ? "USER" : dto.role.toUpperCase();
    return users.save(new User(dto.email, encoder.encode(dto.password), Set.of(role)));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    users.deleteById(id);
  }
}
