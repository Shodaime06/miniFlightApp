package com.miniflight.booking;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long flightId;

  @Column(nullable = false)
  private int seatCount;

  @Column(nullable = false)
  private int totalPrice;

  @Column(nullable = false)
  private Instant createdAt;

  public Booking() {}

  public Booking(Long userId, Long flightId, int seatCount, int totalPrice, Instant createdAt) {
    this.userId = userId;
    this.flightId = flightId;
    this.seatCount = seatCount;
    this.totalPrice = totalPrice;
    this.createdAt = createdAt;
  }

  public Long getId() { return id; }
  public Long getUserId() { return userId; }
  public Long getFlightId() { return flightId; }
  public int getSeatCount() { return seatCount; }
  public int getTotalPrice() { return totalPrice; }
  public Instant getCreatedAt() { return createdAt; }
}
