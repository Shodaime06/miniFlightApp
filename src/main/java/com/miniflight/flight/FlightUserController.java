package com.miniflight.flight;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/flights")
public class FlightUserController {

  private final FlightRepository flights;

  public FlightUserController(FlightRepository flights) {
    this.flights = flights;
  }

  @GetMapping
  public List<Flight> list() {
    return flights.findAll();
  }

  @GetMapping("/{id}")
  public Flight one(@PathVariable Long id) {
    return flights.findById(id).orElseThrow();
  }
}
