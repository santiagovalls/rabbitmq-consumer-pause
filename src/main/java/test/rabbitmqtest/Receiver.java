package test.rabbitmqtest;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  @RabbitListener(queues = "rabbitmq-compose-test")
  public void onMessageReceived(Message message) {
    System.out.println(
        "Received <" + message.getBody() + "> - Priority <" + message.getMessageProperties()
            .getPriority() + ">");
  }
}