package test.rabbitmqtest;

import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class Application {

  @Value("${spring.rabbitmq.concurrentConsumers}")
  private Integer BROKER_CONCURRENT_CONSUMERS;

  @Value("${spring.rabbitmq.maxConcurrentConsumers}")
  private Integer BROKER_MAX_CONCURRENT_CONSUMERS;

  @Value("${spring.rabbitmq.listener.retry.max-attempts}")
  private Integer CONSUMER_MAX_RETRIES;

  @Value("${spring.rabbitmq.addresses}")
  private String addresses;

  @Value("${spring.rabbitmq.host}")
  private String rabbitHost;

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryWithTLS(
      CachingConnectionFactory connectionFactory) {

    if (StringUtils.isEmpty(addresses)) {
      throw new IllegalArgumentException("Addresses must not be null or empty.");
    }

    connectionFactory.setUri(addresses);

    SSLContext sslContext;

    try {
      sslContext = SSLContext.getInstance("TLSv1.2");
      sslContext.init(null, null, null);

      SSLContext.setDefault(sslContext);
      SSLParameters sslParameters = sslContext.getDefaultSSLParameters();

      List<SNIServerName> sniHostNames = new ArrayList<>(1);
      sniHostNames.add(new SNIHostName(rabbitHost));
      sslParameters.setServerNames(sniHostNames);

      SSLSocketFactory wrappedSSLSocketFactory = new SSLSocketFactoryWrapper(
          sslContext.getSocketFactory(), sslParameters);
      connectionFactory.getRabbitConnectionFactory().setSocketFactory(wrappedSSLSocketFactory);

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }

    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setConcurrentConsumers(BROKER_CONCURRENT_CONSUMERS);
    factory.setMaxConcurrentConsumers(BROKER_MAX_CONCURRENT_CONSUMERS);
    factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
    factory.setAutoStartup(true);
    return factory;
  }

  // @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      CachingConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setConcurrentConsumers(BROKER_CONCURRENT_CONSUMERS);
    factory.setMaxConcurrentConsumers(BROKER_MAX_CONCURRENT_CONSUMERS);
    factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
    factory.setAutoStartup(true);
    return factory;
  }

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(Application.class, args);
  }
}