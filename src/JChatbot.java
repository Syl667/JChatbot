import java.util.Hashtable;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.Random;

public class JChatbot {
  private Hashtable<String, HashSet<Integer>> registered_keys;
  private Hashtable<Integer, Answer> answers;
  private Consumer<String> defaultAnswer;
  private Boolean debug = false;
  
  public JChatbot(Consumer<String> defaultAnswer) {
    registered_keys = new Hashtable<String, HashSet<Integer>>();
    answers = new Hashtable<Integer, Answer>();
    this.defaultAnswer = defaultAnswer;
  }
  
  public int addAnswer(String[][] necessary_keys, String[] optional_keys, Consumer<String> onAnswer) {
    int id = answers.size();
    while (answers.containsKey(id)) {
      id -= 1;
    }
    Answer a = new Answer(id, necessary_keys, onAnswer);
    answers.put(id, a);
    for (String[] keys: necessary_keys) {
      for (String key: keys) {
        if (!registered_keys.containsKey(key)) {
          registered_keys.put(key, new HashSet<Integer>());
        }
        registered_keys.get(key).add(id);
      }
    }
    for (String key: optional_keys) {
      if (!registered_keys.containsKey(key)) {
        registered_keys.put(key, new HashSet<Integer>());
      }
      registered_keys.get(key).add(id);
    } 
    return id;
  }
  
  public int calcAnswer(String statement) {
    if (statement.equals("setDebugOn")) {
      this.debug = true;
      return -1;
    }    
    if (statement.equals("setDebugOff")) {
      debug = false;
      return -1;
    } 
    statement = statement.toLowerCase().replaceAll("[^a-z0-9]", " ");
    while (statement.contains("  ")) {
      statement = statement.replace("  ", " ");
    }
    String[] keys = statement.split(" ");
    Hashtable<Integer, Float> probs = new Hashtable<Integer, Float>();
    for (String key: keys) {
      if (registered_keys.containsKey(key)) {
        for (Integer id: registered_keys.get(key)) {
          if (!probs.containsKey(id)) probs.put(id, 0f);
          probs.put(id, probs.get(id) + 1f/(float)keys.length);
          if (this.debug) System.out.println("Answer "+id+" has probability "+probs.get(id));
        }
      }
    }
    int best = getBestAnswer(probs);
    if (best == -1) {
      defaultAnswer.accept(statement);
      return -1;
    }  
    Answer a = answers.get(best);
    while (!a.isValid(keys)) {
      probs.remove(best);
      best = getBestAnswer(probs);
      if (best == -1) {
        defaultAnswer.accept(statement);
        return -1;
      }  
      a = answers.get(best); 
    } 
    a.onAnswer.accept(statement);
    return best;
  }
  
  private Integer getBestAnswer(Hashtable<Integer, Float> probs) {
    float best_prob = 0;
    int best_id = -1;
    for (int id: probs.keySet()) {
      if (probs.get(id) > best_prob) {
        best_prob = probs.get(id);
        best_id = id;
      } 
      else if (!(probs.get(id) < best_prob)) {
        Random random = new Random();
        best_id = random.nextBoolean() ? best_id : id;
        best_prob = probs.get(best_id);
      } // end of if-else
    } 
    if (best_prob < .5f) {
      return -1;
    }
    return best_id;
  }
}

class Answer {
  private String[][] keys;
  
  public int id;
  public Consumer<String> onAnswer;
  
  public Answer(int id, String[][] keys, Consumer<String> onAnswer) {
    this.id = id;
    this.onAnswer = onAnswer;
    this.keys = keys;
  }
  
  public Boolean isValid(String[] input) {
    Boolean valid = false;
    for (String[] ands: keys) {
      valid = true;
      for (String key: ands) {
        if (!Arrays.asList(input).contains(key)) {
          valid = false;
          break;
        }
      }
      if (valid) return true;
    }
    return false;
  }
}