package com.praveen.sqs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation implements Serializable {

  @Serial private static final long serialVersionUID = -3409437283327706280L;

  @Column(name = "latitude")
  private String latitude;

  @Column(name = "longitude")
  private String longitude;
}
