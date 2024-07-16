package com.praveen.sqs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SqsPocApplication {

  public static void main(String[] args) {
    SpringApplication.run(SqsPocApplication.class, args);
  }
}
