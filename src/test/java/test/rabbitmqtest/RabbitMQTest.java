package test.rabbitmqtest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RabbitMQTest {

  private static ConsumerThread firstConsumer = new ConsumerThread();

  public static void main(String[] args) {
    initializeProducers();
    initializeConsumers();
    waitForIt();
  }

  private static void initializeProducers() {
    ExecutorService producers = Executors.newFixedThreadPool(5);
    for (int i = 0; i < 5; i++) {
      Runnable producer = new ProducerThread("ProducerThreadMessage", 1000);
      producers.execute(producer);
    }
  }

  private static void initializeConsumers() {
    new Thread(firstConsumer).start();
  }

  private static void waitForIt() {
    while (true) {
      try {
        Thread.sleep(10000);
        firstConsumer.cancelConsumer();
        System.out.println("Cancel!");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
