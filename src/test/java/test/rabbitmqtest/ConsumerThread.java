package test.rabbitmqtest;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerThread implements Runnable {

  private Channel channel;
  private Connection connection;

  @Override
  public void run() {
    try {
      connectToQueue();
      registerConsumer();
      waitForIt();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      disconnectFromQueue();
    }
  }

  private void disconnectFromQueue() {
    try {
      channel.close();
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void connectToQueue() throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    connection = factory.newConnection();
    channel = connection.createChannel();
    channel.queueDeclare("testQueue", false, false, false, null);
  }

  private void registerConsumer() throws IOException, TimeoutException {
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
          AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume("testQueue", true, consumer);
  }

  public void cancelConsumer() throws IOException, TimeoutException {
    channel.close();
  }

  private void waitForIt() {
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}