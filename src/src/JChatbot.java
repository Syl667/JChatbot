import java.util.Hashtable;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.Random;

public class JChatbot {
  private Hashtable<String, HashSet<Integer>> registered_keys; // All known Tokens and IDs of possible Answers
  private Hashtable<Integer, Answer> answers; // All known Answers
  private Boolean debug = false;
  
  public JChatbot(Consumer<String> defaultAnswer) {
    registered_keys = new Hashtable<String, HashSet<Integer>>();
    answers = new Hashtable<Integer, Answer>();
    this.setDefaultAnswer(defaultAnswer);
  }
  
  public JChatbot() {
    this((statement) -> {});
  }
  
  public int addAnswer(String[][] necessary_keys, String[] optional_keys, Consumer<String> onAnswer, Float min_prob) {
    int id = answers.size()-1;
    // To ensure that every ID is used if an Answer has been removed
    while (answers.containsKey(id)) {
      id -= 1;
    }
    Answer a = new Answer(id, necessary_keys, onAnswer, min_prob);
    answers.put(id, a);
    // Register all necessary and optional keys in registered_keys to calculate the Probability
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
  
  public int addAnswer(String[][] necessary_keys, String[] optional_keys, Consumer<String> onAnswer) {
    // Add a new Answer with a default minimum Probability of 50%
    return this.addAnswer(necessary_keys, optional_keys, onAnswer, .5f);
  }
  
  public void setDefaultAnswer(Consumer<String> defaultAnswer) {      
    // The default Answer is registered as normal Answer too, but is only chosen if there is no other choice
    this.answers.put(-1, new Answer(-1, new String[0][0], defaultAnswer, 1f));
  }
  
  public void removeAnswer(int id) {
    // The default Answer is not really deleted, it just does nothing so that there still is something to answer
    if (id == -1) {
      this.setDefaultAnswer((statement) -> {});
      return;
    }
    this.answers.remove(id);
    // Remove Answer from every registered Token
    for (String key: this.registered_keys.keySet()) {
      this.registered_keys.get(key).remove(id);
      // If there is now no Answer for the Token, it is removed to use less space
      if (this.registered_keys.get(key).isEmpty())
      this.registered_keys.remove(key);
    }
  }
  
  public int calcAnswer(String statement) {
    // To distinguish default Answer and Debug toggle, they get own IDs
    if (statement.equals("setDebugOn")) {
      this.debug = true;
      return -2;
    }
    if (statement.equals("setDebugOff")) {
      debug = false;
      return -3;
    }
    // Only lowercase and numbers are currently used for tokens
    statement = statement.toLowerCase().replaceAll("[^a-z0-9]", " ");
    while (statement.contains("  "))
    statement = statement.replace("  ", " ");
    
    String[] keys = statement.split(" ");
    Hashtable<Integer, Float> probs = new Hashtable<Integer, Float>();
    // The Probability for the default Answer is 0% so that it will be selected at the end
    probs.put(-1, 0f);
    for (String key: keys) {
      if (registered_keys.containsKey(key)) {
        for (Integer id: registered_keys.get(key)) {
          // The Probabilities are calculated with how many tokens (necessary or optional) in a statement fit an Answer
          if (!probs.containsKey(id)) probs.put(id, 0f);
          probs.put(id, probs.get(id) + 1f/(float)keys.length);
          if (this.debug) System.out.println("Answer "+id+" has probability "+probs.get(id));
        }
      }
    }
    // Search for the best valid Answer while there are Answers available (default Answer excluded)
    int best = getBestAnswer(probs);
    Answer a = answers.get(best);
    while (!a.isValid(keys, probs.get(best)) && !(probs.size()==1)) {
      if (this.debug) System.out.println("Answer "+best+" is not valid");
      if (best > -1) probs.remove(best);
      best = getBestAnswer(probs);
      a = answers.get(best);
    }
    // Execute onAnswer
    a.onAnswer.accept(statement);
    if (this.debug) System.out.println("Answer "+best+" has been chosen");
    return best;
  }
  
  private Integer getBestAnswer(Hashtable<Integer, Float> probs) {
    float best_prob = 0;
    int best_id = -1;
    // Search for the Answer with the highest Probability
    for (int id: probs.keySet()) {
      if (probs.get(id) > best_prob) {
        best_prob = probs.get(id);
        best_id = id;
      }
      // If two Answers approximately have the same Probability, a random Answer is chosen
      else if (probs.get(id).equals(best_prob)) {
        Random random = new Random();
        best_id = random.nextBoolean() ? best_id : id;
        best_prob = probs.get(best_id);
      }
    }
    return best_id;
  }
}

class Answer {
  // Tokens that are necessary for the Answer to be valid; the Array works like a DNF
  private String[][] keys;
  
  public int id;
  public Consumer<String> onAnswer;
  
  private Float min_prob;
  
  public Answer(int id, String[][] keys, Consumer<String> onAnswer, Float min_prob) {
    this.id = id;
    this.onAnswer = onAnswer;
    this.keys = keys;
    this.min_prob = min_prob;
  }
  
  public Boolean isValid(String[] input, Float prob) {
    // An Answer is valid if the Probability equals or is greater than the minimum Probability and
    // all necessary keys are in the statement
    if (prob < min_prob) return false;
    Boolean valid = true;
    for (String[] ands: keys) {
      valid = true;
      for (String key: ands) {
        if (!Arrays.asList(input).contains(key.toLowerCase())) {
          valid = false;
          break;
        }
      }
      if (valid) return true;
    }
    return valid;
  }
}
