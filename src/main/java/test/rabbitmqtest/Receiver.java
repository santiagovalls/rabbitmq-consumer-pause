package test.rabbitmqtest;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  @RabbitListener(queues = "rabbitmq-compose-test")
  public void onMessageReceived(Message message) {
    System.out.printf("Received message: %s%n", new String(message.getBody()));
  }
}