package test.rabbitmqtest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartStopController {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @RequestMapping("/publishTestMessage")
  public String publishTestMessage() {
    rabbitTemplate.convertAndSend("rabbitmq-compose-test", "Test Message");
    return "publishTestMessage";
  }
}
