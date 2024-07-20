package com.praveen.sqs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "company")
public class CompanyEntity implements Serializable {

  @Serial private static final long serialVersionUID = -8871480923376789073L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id_generator")
  @SequenceGenerator(name = "company_id_generator", sequenceName = "company_id_sequence")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "catch_phrase")
  private String catchPhrase;

  @Column(name = "tag_line")
  private String tagLine;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;
}
