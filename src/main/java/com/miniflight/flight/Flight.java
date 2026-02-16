package com.miniflight.flight;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
public class Flight {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String fromCity;

  @Column(nullable = false)
  private String toCity;

  @Column(nullable = false)
  private LocalDateTime departureTime;

  @Column(nullable = false)
  private int capacity;

  @Column(nullable = false)
  private int seatsAvailable;

  @Column(nullable = false)
  private int price;

  public Flight() {}

  public Flight(String fromCity, String toCity, LocalDateTime departureTime, int capacity, int seatsAvailable, int price) {
    this.fromCity = fromCity;
    this.toCity = toCity;
    this.departureTime = departureTime;
    this.capacity = capacity;
    this.seatsAvailable = seatsAvailable;
    this.price = price;
  }

  public Long getId() { return id; }
  public String getFromCity() { return fromCity; }
  public String getToCity() { return toCity; }
  public LocalDateTime getDepartureTime() { return departureTime; }
  public int getCapacity() { return capacity; }
  public int getSeatsAvailable() { return seatsAvailable; }
  public int getPrice() { return price; }

  public void setFromCity(String fromCity) { this.fromCity = fromCity; }
  public void setToCity(String toCity) { this.toCity = toCity; }
  public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
  public void setCapacity(int capacity) { this.capacity = capacity; }
  public void setSeatsAvailable(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }
  public void setPrice(int price) { this.price = price; }
}
