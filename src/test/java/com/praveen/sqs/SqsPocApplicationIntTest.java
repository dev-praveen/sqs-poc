package com.praveen.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.sqs.dao.UserRepository;
import com.praveen.sqs.model.User;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class SqsPocApplicationIntTest {

  @Container
  static LocalStackContainer localstack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
          .withServices(LocalStackContainer.Service.SQS);

  @Autowired private SqsTemplate sqsTemplate;
  @Autowired private UserRepository userRepository;

  @Value("${sqs.queue.url}")
  private String queueEndPoint;

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

  @Test
  void localStackShouldCreateAndRun() {

    assertThat(localstack.isCreated()).isTrue();
    assertThat(localstack.isRunning()).isTrue();
  }

  @Test
  void shouldReceiveUserAndSaveInDatabase() throws JsonProcessingException {

    final var user = getUserJson();
    final var uuid = UUID.randomUUID().toString();
    sqsTemplate.send(to -> to.queue(queueEndPoint).payload(user).header("uuid", uuid));
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () -> {
              final var userEntities = userRepository.findAll();
              assertThat(userEntities).isNotEmpty();
              assertThat(userEntities).hasSize(1);

              final var userEntity = userEntities.get(0);
              assertThat(userEntity.getCorrelationId()).isEqualTo(uuid);
            });
  }
}
