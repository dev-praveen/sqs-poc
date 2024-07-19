package com.praveen.sqs.service;

import com.praveen.sqs.model.User;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SQSReceiver {

  @SqsListener(
      value = "${sqs.queue.url}",
      acknowledgementMode = SqsListenerAcknowledgementMode.MANUAL)
  public void receiveMessage(@Payload User user, Acknowledgement acknowledgement) {

    log.info("Message received from SQS:: {}", user);
    acknowledgement.acknowledge();
  }
}
