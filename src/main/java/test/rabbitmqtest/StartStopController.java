package test.rabbitmqtest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartStopController {

  public static boolean RECEIVER_THROW_EXCEPTION_FLAG = false;

  @Autowired
  @Qualifier("containerPause")
  SimpleMessageListenerContainer container;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @RequestMapping("/startConsumer")
  public String startConsumer() {
    container.start();
    return "startConsumer";
  }

  @RequestMapping("/stopConsumer")
  public String stopConsumer() {
    container.stop();
    return "stopConsumer";
  }

  @RequestMapping("/publishTestMessage")
  public String publishTestMessage() {
    rabbitTemplate.convertAndSend(Application.queueName, "Test Message");
    return "publishTestMessage";
  }

  @RequestMapping("/configThrowException")
  public String throwException(@RequestParam boolean value) {
    StartStopController.RECEIVER_THROW_EXCEPTION_FLAG = value;
    return "configThrowException: " + Boolean
        .toString(StartStopController.RECEIVER_THROW_EXCEPTION_FLAG);
  }
}
