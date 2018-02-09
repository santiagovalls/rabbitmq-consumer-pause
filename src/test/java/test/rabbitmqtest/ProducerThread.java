package test.rabbitmqtest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerThread implements Runnable {

  private String message;
  private long sleepTime;
  private Channel channel;
  private Connection connection;

  public ProducerThread(String message, long sleepTime) {
    this.message = message;
    this.sleepTime = sleepTime;
  }

  @Override
  public void run() {
    try {
      connectToQueue();
      while (true) {
        pushToQueue();
        sleepThread();
      }
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

  private void pushToQueue() throws IOException, TimeoutException {
    channel.basicPublish("", "testQueue", null, message.getBytes());
  }

  private void sleepThread() throws InterruptedException {
    Thread.sleep(sleepTime);
  }
}