package az.caspian.nserv;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    new CaspianHttpServer().start();
    Thread.currentThread().join();
  }
}
