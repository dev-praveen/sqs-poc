package com.praveen.sqs.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SQSReceiver {

  @SqsListener(
      value = "${sqs.queue.url}",
      acknowledgementMode = SqsListenerAcknowledgementMode.MANUAL)
  public void receiveMessage(String message, Acknowledgement acknowledgement) {

    log.info("Message received from SQS:: {}", message);
    /*if (!message.isEmpty()) {
      throw new RuntimeException("Experimenting....");
    }*/
    acknowledgement.acknowledge();
  }
}
