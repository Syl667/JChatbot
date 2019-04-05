import java.util.function.Consumer;
import java.util.Scanner;

public class TestChatbot { 
  
  public static void main(String[] args) {
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
    
    int exit_id = bot.addAnswer(exit_keys, new String[0], (statement) -> System.out.println("Bye bye")); 
    bot.addAnswer(hello_keys, new String[]{"world"}, (statement) -> System.out.println("Hello World!"));
    
    Scanner scan = new Scanner(System.in);
    while (true) { 
      System.out.print(">> ");
      String input = scan.nextLine();
      int id = bot.calcAnswer(input);
      if (id == exit_id) {
        break;
      } // end of if
    } 
  }
  
} 
