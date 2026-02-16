package com.miniflight.auth;

import com.miniflight.auth.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService auth;

  @Value("${app.cookie.refresh-name}") private String refreshCookieName;
  @Value("${app.cookie.secure}") private boolean cookieSecure;
  @Value("${app.cookie.same-site}") private String sameSite;
  @Value("${app.jwt.refresh-days}") private int refreshDays;

  public AuthController(AuthService auth) {
    this.auth = auth;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
    auth.register(req.email, req.password);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req, HttpServletResponse res) {
    var result = auth.login(req.email, req.password);
    setRefreshCookie(res, result.refreshToken());
    return ResponseEntity.ok(new AuthResponse(result.accessToken()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "${app.cookie.refresh-name}", required = false) String rt,
                                              HttpServletResponse res) {
    if (rt == null || rt.isBlank()) return ResponseEntity.status(401).build();
    var result = auth.refresh(rt);
    setRefreshCookie(res, result.refreshToken());
    return ResponseEntity.ok(new AuthResponse(result.accessToken()));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@CookieValue(name = "${app.cookie.refresh-name}", required = false) String rt,
                                  HttpServletResponse res) {
    auth.logout(rt);
    clearRefreshCookie(res);
    return ResponseEntity.ok().build();
  }

  private void setRefreshCookie(HttpServletResponse res, String value) {
    int maxAge = (int) Duration.ofDays(refreshDays).toSeconds();
    res.addHeader("Set-Cookie",
        String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; %s; SameSite=%s",
            refreshCookieName,
            value,
            maxAge,
            cookieSecure ? "Secure" : "",
            sameSite
        )
    );
  }

  private void clearRefreshCookie(HttpServletResponse res) {
    res.addHeader("Set-Cookie",
        String.format("%s=; Max-Age=0; Path=/; HttpOnly; %s; SameSite=%s",
            refreshCookieName,
            cookieSecure ? "Secure" : "",
            sameSite
        )
    );
  }
}
