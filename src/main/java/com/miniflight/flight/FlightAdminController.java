package com.miniflight.flight;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/flights")
public class FlightAdminController {

  private final FlightRepository flights;

  public FlightAdminController(FlightRepository flights) {
    this.flights = flights;
  }

  @GetMapping
  public List<Flight> all() {
    return flights.findAll();
  }

  public static class FlightCreateUpdate {
    @NotBlank public String fromCity;
    @NotBlank public String toCity;
    public String departureTime; // ISO: 2026-02-01T15:30
    @Min(1) public int capacity;
    @Min(0) public int seatsAvailable;
    @Min(0) public int price;
  }

  @PostMapping
  public Flight create(@RequestBody FlightCreateUpdate dto) {
    LocalDateTime dt = LocalDateTime.parse(dto.departureTime);
    int seats = Math.min(dto.seatsAvailable, dto.capacity);
    Flight f = new Flight(dto.fromCity, dto.toCity, dt, dto.capacity, seats, dto.price);
    return flights.save(f);
  }

  @PutMapping("/{id}")
  public Flight update(@PathVariable Long id, @RequestBody FlightCreateUpdate dto) {
    Flight f = flights.findById(id).orElseThrow();
    f.setFromCity(dto.fromCity);
    f.setToCity(dto.toCity);
    f.setDepartureTime(LocalDateTime.parse(dto.departureTime));
    f.setCapacity(dto.capacity);
    f.setSeatsAvailable(Math.min(dto.seatsAvailable, dto.capacity));
    f.setPrice(dto.price);
    return flights.save(f);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    flights.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
