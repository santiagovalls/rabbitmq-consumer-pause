package test.rabbitmqtest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartStopController {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @RequestMapping("/publishTestMessages")
  public String publishTestMessages() {
    for (int i = 0; i < 10; i++) {
      final int priority = i;
      rabbitTemplate.convertAndSend(
          "rabbitmq-compose-test",
          "Test Message: " + priority,
          message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
          }
      );
    }
    return "publishTestMessages";
  }
}
