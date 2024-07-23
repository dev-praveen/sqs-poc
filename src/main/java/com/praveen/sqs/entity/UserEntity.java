package com.praveen.sqs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "user_data")
public class UserEntity implements Serializable {

  @Serial private static final long serialVersionUID = 468343573916652744L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
  @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_sequence")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "website")
  private String website;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private AddressEntity address;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private CompanyEntity company;

  @Column(name = "correlation_id", nullable = false, unique = true)
  private String correlationId;

  public void setAddress(AddressEntity address) {
    address.setUser(this);
    this.address = address;
  }

  public void setCompany(CompanyEntity company) {
    company.setUser(this);
    this.company = company;
  }
}
