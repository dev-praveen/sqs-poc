package com.praveen.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.sqs.dao.UserRepository;
import com.praveen.sqs.model.User;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
class SQSEMQTest {

  private static SQSRestServer server;

  @Value("${sqs.queue.url}")
  private String queue;

  @Autowired private SqsTemplate sqsTemplate;
  @Autowired private UserRepository userRepository;

  @BeforeAll
  static void startElasticMq() {
    server = SQSRestServerBuilder.withPort(9324).start();
  }

  @AfterAll
  static void stopElasticMq() {

    /* This approach is stopping the ElasticMQ server before the listener acknowledges the received message,
    resulting in the message remaining unacknowledged. Although the test itself does not fail,
    it leaves the message processing incomplete. */
    server.stopAndWait();
  }

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void shouldReceiveUserAndSaveInDatabase() throws JsonProcessingException {

    final var user = getUserJson();
    final var uuid = UUID.randomUUID().toString();
    sqsTemplate.send(to -> to.queue(queue).payload(user).header("uuid", uuid));
    await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(
            () -> {
              final var userEntities = userRepository.findAll();
              assertThat(userEntities).isNotEmpty();
              assertThat(userEntities).hasSize(1);

              final var userEntity = userEntities.get(0);
              assertThat(userEntity.getCorrelationId()).isEqualTo(uuid);
            });
  }

  private User getUserJson() throws JsonProcessingException {

    String json =
        """
                      {
                        "name": "Ervin Howell",
                        "username": "Antonette",
                        "email": "Shanna@melissa.tv",
                        "address": {
                          "street": "Victor Plains",
                          "suite": "Suite 879",
                          "city": "Wisokyburgh",
                          "zipcode": "90566-7771",
                          "geo": {
                            "lat": "-43.9509",
                            "lng": "-34.4618"
                          }
                        },
                        "phone": "010-692-6593 x09125",
                        "website": "anastasia.net",
                        "company": {
                          "name": "Deckow-Crist",
                          "catchPhrase": "Proactive didactic contingency",
                          "bs": "synergize scalable supply-chains"
                        }
                      }
                      """;
    final var objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, User.class);
  }

  @TestConfiguration
  static class SQSConfig {

    @Bean
    SqsAsyncClient sqsAsyncClient() {
      return SqsAsyncClient.builder()
          .endpointOverride(URI.create("http://localhost:9324")) // ElasticMQ endpoint
          .region(Region.US_EAST_1) //
          .credentialsProvider(
              StaticCredentialsProvider.create(
                  AwsBasicCredentials.create("access-key", "secret-key")))
          .build();
    }

    @Bean
    SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
      return SqsTemplate.newTemplate(sqsAsyncClient);
    }
  }
}
