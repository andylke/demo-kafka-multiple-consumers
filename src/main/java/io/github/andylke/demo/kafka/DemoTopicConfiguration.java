package io.github.andylke.demo.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration(proxyBeanMethods = false)
class DemoTopicConfiguration {

  @Value("${demo.topic}")
  private String topic;

  @Value("${demo.topic-partition-size}")
  private int topicPartitionSize;

  @Bean
  public NewTopic fooTopic() {
    return TopicBuilder.name(topic).partitions(topicPartitionSize).build();
  }
}
