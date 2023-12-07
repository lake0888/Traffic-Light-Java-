package traffic;

import java.io.IOException;
import java.util.Scanner;

public class Main {
  final static Scanner scanner = new Scanner(System.in);
  public static void main(String[] args) throws InterruptedException {
    greet();
    //ROADS
    System.out.print("Input the number of roads: ");
    int roads = getInt();
    //INTERVAL
    System.out.print("Input the interval: ");
    int interval = getInt();
    QueueThread thread = new QueueThread(roads, interval);
    thread.setName("QueueThread");
    thread.start();
    thread.clearOutput();
    scanner.nextLine();
    menu(roads, interval, thread);
  }

  public static void greet() {
    System.out.println("Welcome to the traffic management system!");
  }

  public static int getInt() {
    boolean flag = true;
    int value = 0;
    while(flag) {
      try {
        value = Integer.parseInt(scanner.next());
        if (value > 0) flag = false;
        else throw new NumberFormatException();
      } catch (NumberFormatException ex) {
        System.out.print("Error! Incorrect input. Try again: ");
        continue;
      }
    }
    return value;
  }
  public static void menu(int roads, int interval, QueueThread thread) throws InterruptedException {
    String menu = """
            Menu:
            1. Add road
            2. Delete road
            3. Open system
            0. Quit
            """;

    int option = -1;
    while (option != 0) {
      if (option != 3) System.out.print(menu);
      try {
        String value = scanner.nextLine();
        if (!value.isEmpty()) {
          option = Integer.parseInt(value);
          switch (option) {
            case 1 -> {
              thread.addRoads();
              scanner.nextLine();
            }
            case 2 -> {
              thread.deleteRoads();
              scanner.nextLine();
            }
            case 3 -> thread.setVisible(true);
            case 0 -> quit(thread);
            default -> {
              throw new NumberFormatException();
            }
          }
        } else {
          thread.setVisible(false);
          option = -1;
        }
      } catch(NumberFormatException ex) {
        System.out.println("Incorrect option");
        scanner.nextLine();
        continue;
      } finally {
        if (option != 3) thread.clearOutput();
      }
    }
  }

  public static void quit(QueueThread thread) throws InterruptedException {
    //STOP QueueThread
    thread.setOn(false);
    thread.join();
    System.out.println("Bye!");
  }
}
