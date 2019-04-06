# JChatbot

JChatbot is a simple Chatbot for Java applications. You can add custom Answers, that are calculated with Probabilities and required Tokens.

***Constructor Summary***

Constructor and Description|
---------------------------|
*JChatbot(Consumer`<String>` defaultAnswer)* <br> Constructs a new JChatbot <br> **Parameters:** <br> *defaultAnswer* - gets your input as parameter passed and is executed when the Chatbox does not find an adequate Answer.|
*JChatbot()* <br> Constructs a new JChatbot where the default Answer does nothing |
  
***Methods Summary***

Modifier and Type | Method and Description
------------------|-----------------------
  *int*           | *addAnswer(String[][] required_keys, String[] optional_keys, Consumer`<String>` onAnswer, Float min_prob)* <br> Adds a possible Answer to the Chatbot. <br> **Parameters:** <br> *required_keys* - all tokens that are required to give the Answer. The Array works like a DNF, so all Arrays are connected with an OR and all Strings inside an Array are connected with an AND. <br> *optional_keys* - all Tokens that can be used to increase the Probability of the Answer. <br> *onAnswer* - works like *defaultAnswer* in the Constructor but is only executed when the Answer is chosen. <br> *min_prob* - the smallest Probability so that the Answer can be chosen. <br> **Returns:** <br> ID of the added Answer.
  *int*          | *addAnswer(String[][] required_keys, String[] optional_keys, Consumer`<String>` onAnswer)* <br> Adds a possible Answer to the Chatbot. <br> **Parameters:** <br> See above. <br> **Returns:** <br> *addAnswer(necessary_keys, optional_keys, onAnswer, .5f)*
  *void*         | *setDefaultAnswer(Consumer`<String>` defaultAnswer)* <br> Sets the Answer that will be chosen if no other Answer is valid. <br> **Parameters:** <br> *defaultAnswer* - the Lambda Function that is executed when the Answer is chosen. It gets the input as Parameter.
  *void*         | *removeAnswer(int id)* <br> Removes an Answer. <br> **Parameters:** <br> *id* - the ID of the Answer to be removed.
  *int*          | *calcAnswer(String statement)* <br> Calculates and executes the best Answer for a given input Statement. <br> **Parameters:** <br> *statement* - your input. <br> **Returns:** <br> ID of the chosen Answer and -1 if no Answer was found.
  
  
  
Note: Debug Mode can be turned on and off if you enter setDebugOn or setDebugOff.


## How to use in your Java Project:
1. Add src/JChatbot.java to your Project
2. Create an Instance of JChatbot (see above)
3. Add custom Answers (also see above)
4. Have fun!

If you have Problems instantiating or adding Answers see src/TestChatbot.java for a sample Chatbot



Feedback and constructive Criticism are welcome.
