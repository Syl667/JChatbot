import java.util.Hashtable;

public class JChatbot() {
    private Hashtable<String, Integer[]> registered_keys;
    private Hashtable<Integer, Answer> answers;

    public void addAnswer(String[][] necessary_keys, String[] optional_keys, Consumer<String> onAnswer) {
        
    }

    public void calcAnswer(String statement) {
        statement = statement.toLower().replaceAll("[^a-z0-9]", " ");
        while (statement.contains("  ")) {
            statement.replace("  ", " ");
        }
        String[] keys = statement.split(" ");
        Hashtable<Integer, Float> ids = new Hashtable<Integer, Float>();
        for (String key: keys) {
            if (registered_keys.containsKey(key)) {
                for (Integer id: registered_keys.values()) {
                    if (!ids.containsKey(id)) ids.put(id, 0f);
                    ids.put(id, ids.get(id) + 1/keys.length());
                }
            }
        }
    }
}

public class Answer {
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
                 if (!input.contains(key)) {
                     valid = false;
                     break;
                 }
             }
             if (valid) return true;
         }
         return false;
     }
}
