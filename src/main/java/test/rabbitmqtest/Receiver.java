package test.rabbitmqtest;

public class Receiver {

  private String name;

  public Receiver(String name) {
    this.name = name;
  }

  public void receiveMessage(String message) {
    System.out.println("Receiver <" + name + "> - Received <" + message + ">");
    sleepTenSeconds();
    if (!StartStopController.RECEIVER_THROW_EXCEPTION_FLAG) {
      System.out.println("Receiver <" + name + "> - Finish!");
    } else {
      throw new RuntimeException("POC Receiver Exception!");
    }
  }

  private void sleepTenSeconds() {
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}