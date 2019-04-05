import java.util.Hashtable;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.Arrays;

public class JChatbot {
    private Hashtable<String, HashSet<Integer>> registered_keys;
    private Hashtable<Integer, Answer> answers;

    public JChatbot() {
        registered_keys = new Hashtable<String, HashSet<Integer>>();
        answers = new Hashtable<Integer, Answer>();
    }

    public void addAnswer(String[][] necessary_keys, String[] optional_keys, Consumer<String> onAnswer) {
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
    }

    public void calcAnswer(String statement) {
        statement = statement.toLowerCase().replaceAll("[^a-z0-9]", " ");
        while (statement.contains("  ")) {
            statement.replace("  ", " ");
        }
        String[] keys = statement.split(" ");
        Hashtable<Integer, Float> probs = new Hashtable<Integer, Float>();
        for (String key: keys) {
            if (registered_keys.containsKey(key)) {
                for (Integer id: registered_keys.get(key)) {
                    if (!probs.containsKey(id)) probs.put(id, 0f);
                    probs.put(id, probs.get(id) + 1/keys.length);
                }
            }
        }
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
