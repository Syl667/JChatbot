import java.util.function.Consumer;
import java.util.Scanner;

public class TestChatbot {

  public static void main(String[] args) {
    // Create a new Instance of JChatbot with a default Answer
    JChatbot bot = new JChatbot((statement) -> System.out.println("..."));
    String [][] exit_keys = {
      {
        "exit"
      },
      {
        "stop"
      },
      {
        "quit"
      }
    };
    String [][] hello_keys = {
      {
        "hello"
      },
      {
        "hi"
      }
    };
    String[][] sense = {
      {
        "sense",
        "life"
      }
    };
    String[] sense_optional = {
      "what",
      "is",
      "the",
      "of"
    };

    // Add a new Answer and save its ID
    int exit_id = bot.addAnswer(exit_keys, new String[0], (statement) -> System.out.println("Bye bye"), .6f);
    // Add a new Answer
    bot.addAnswer(hello_keys, new String[]{"world"}, (statement) -> System.out.println("Hello World!"), .4f);
    // Add a new Answer with a default minimum Probability of 50%
    bot.addAnswer(sense, sense_optional, (statement) -> System.out.println("42"));

    Scanner scan = new Scanner(System.in);
    while (true) {
      System.out.print(">> ");
      String input = scan.nextLine();
      // You can either use onAnswer to execute some Code or switch the IDs;
      // be aware that you can only use final variables in a Lambda Function
      int id = bot.calcAnswer(input);
      if (id == exit_id) {
        break;
      }
    }
  }

}
