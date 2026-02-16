package com.miniflight.booking;

import com.miniflight.flight.Flight;
import com.miniflight.flight.FlightRepository;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/user/bookings")
public class BookingController {

  private final BookingRepository bookings;
  private final FlightRepository flights;

  public BookingController(BookingRepository bookings, FlightRepository flights) {
    this.bookings = bookings;
    this.flights = flights;
  }

  public static class BookRequest {
    public Long flightId;
    @Min(1) public int seatCount;
  }

  @PostMapping
  public Booking book(Authentication auth, @RequestBody BookRequest req) {
    Long userId = Long.valueOf(String.valueOf(auth.getPrincipal()));
    Flight f = flights.findById(req.flightId).orElseThrow();

    if (req.seatCount <= 0) throw new RuntimeException("Seat count must be > 0");
    if (f.getSeatsAvailable() < req.seatCount) throw new RuntimeException("Not enough seats");

    f.setSeatsAvailable(f.getSeatsAvailable() - req.seatCount);
    flights.save(f);

    int total = req.seatCount * f.getPrice();
    Booking b = new Booking(userId, f.getId(), req.seatCount, total, Instant.now());
    return bookings.save(b);
  }

  @GetMapping
  public List<Booking> my(Authentication auth) {
    Long userId = Long.valueOf(String.valueOf(auth.getPrincipal()));
    return bookings.findByUserId(userId);
  }
}
