package io.github.andylke.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DemoConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(DemoConsumer.class);

  @KafkaListener(topics = "${demo.topic}")
  public void receive(String record) throws InterruptedException {
    LOGGER.info("Received [{}]", record);
  }
}
