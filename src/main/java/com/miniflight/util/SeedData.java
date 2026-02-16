package com.miniflight.util;

import com.miniflight.flight.Flight;
import com.miniflight.flight.FlightRepository;
import com.miniflight.user.User;
import com.miniflight.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
public class SeedData {

  @Bean
  CommandLineRunner seed(UserRepository users, FlightRepository flights, PasswordEncoder encoder) {
    return args -> {
      if (!users.existsByEmail("admin@miniflight.com")) {
        users.save(new User("admin@miniflight.com", encoder.encode("admin123"), Set.of("ADMIN")));
      }
      if (!users.existsByEmail("user@miniflight.com")) {
        users.save(new User("user@miniflight.com", encoder.encode("user1234"), Set.of("USER")));
      }

      if (flights.count() == 0) {
        flights.save(new Flight("Ankara", "İstanbul", LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), 100, 100, 900));
        flights.save(new Flight("İzmir", "Berlin", LocalDateTime.now().plusDays(3).withHour(14).withMinute(30), 1500, 1500, 4500));
      }
    };
  }
}
