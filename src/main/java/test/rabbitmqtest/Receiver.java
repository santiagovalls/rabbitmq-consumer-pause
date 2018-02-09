package test.rabbitmqtest;

public class Receiver {

  private String name;

  public Receiver(String name) {
    this.name = name;
  }

  public void receiveMessage(byte[] message) {
    System.out.println("Receiver <" + name + "> - Received <" + new String(message) + ">");
  }
}