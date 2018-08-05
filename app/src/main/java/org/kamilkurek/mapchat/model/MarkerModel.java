package org.kamilkurek.mapchat.model;

import com.github.javafaker.Faker;

import java.io.Serializable;
import java.util.UUID;

public class MarkerModel implements Serializable {

  private transient Faker faker = new Faker();

  private final String id;
  private final double lat;
  private final double lng;
  private final String name;

  public MarkerModel(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
    this.id = UUID.randomUUID().toString();
    this.name = faker.name().firstName();
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean isClickable() {
    return !name.equals("You");
  }
}
