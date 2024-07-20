package com.praveen.sqs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record User(
    String name,
    @JsonProperty("username") String userName,
    String email,
    Address address,
    String phone,
    String website,
    Company company) {

  public record Address(String street, String suite, String city, String zipcode, Geo geo) {}

  public record Geo(@JsonProperty("lat") String latitude, @JsonProperty("lng") String longitude) {}

  public record Company(String name, String catchPhrase, String bs) {}
}
