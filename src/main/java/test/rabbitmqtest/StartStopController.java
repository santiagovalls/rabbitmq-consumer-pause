package test.rabbitmqtest;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartStopController {

  @Autowired
  @Qualifier("containerPause")
  SimpleMessageListenerContainer container;

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
}
