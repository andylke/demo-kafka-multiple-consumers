package io.github.andylke.demo.kafka;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.github.andylke.demo.DemoApplication;

@Component
public class DemoProducer {

  private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Value("${demo.records-per-interval}")
  private int recordsPerInterval;

  @Value("${demo.topic}")
  private String topic;

  @Scheduled(fixedDelay = 20000)
  public void send() {
    final List<CompletableFuture<?>> futures = new ArrayList<CompletableFuture<?>>();

    final long startTime = System.nanoTime();
    for (int index = 0; index < recordsPerInterval; index++) {
      futures.add(
          kafkaTemplate
              .send(topic, "message-" + index + " at " + Instant.now().toString())
              .completable());
    }
    try {
      CompletableFuture.allOf(futures.toArray(size -> new CompletableFuture<?>[size])).join();
    } catch (CompletionException e) {
      LOGGER.error("Failed sending messages", e);
      throw e;
    }

    LOGGER.info(
        "Sent [{}] records to [{}], elapsed [{}]",
        recordsPerInterval,
        topic,
        Duration.ofNanos(startTime - System.nanoTime()));
  }
}
