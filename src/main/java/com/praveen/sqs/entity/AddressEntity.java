package com.praveen.sqs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "address")
public class AddressEntity implements Serializable {

  @Serial private static final long serialVersionUID = 1783665593534257193L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_generator")
  @SequenceGenerator(
      name = "address_id_generator",
      sequenceName = "address_id_sequence",
      initialValue = 2000,
      allocationSize = 100)
  private Long id;

  @Column(name = "street")
  private String street;

  @Column(name = "suite")
  private String suite;

  @Column(name = "city")
  private String city;

  @Column(name = "zipcode")
  private String zipcode;

  @Embedded private GeoLocation geoLocation;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;
}
