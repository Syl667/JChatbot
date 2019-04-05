# JChatbot

JChatbot is an easy Chatbot for Java applications. You can add custom Answers, that are calculated with Probabilities and required Tokens (Note: if the Probability of an Answer is less than 50%, it will not be executed).

***Constructor Summary***

Constructor and Description|
---------------------------|
*JChatbot(Consumer`<String>` defaultAnswer)* <br> Constructs a new JChatbot <br> **Parameters:** <br> defaultAnswer - gets your input as parameter passed and is executed when the Chatbox does not find an adequate Answer.|
  
***Methods Summary***

Modifier and Type | Method and Description
------------------|-----------------------
  *int*           | *addAnswer(String[][] required_keys, String[] optional_keys, Consumer`<String>` onAnswer)* <br> Adds a possible Answer to the Chatbot. <br> **Parameters:** <br> *required_keys* - all tokens that are required to give the Answer. The Array works like a DNF, so all Arrays are connected with an OR and all Strings inside an Array are connected with an AND. <br> optional_keys - all Tokens that can be used to increase the Probability of the Answer. <br> *onAnswer* - works like *defaultAnswer* in the Constructor but is only executed when the Answer is chosen. <br> **Returns:** <br> ID of the added Answer.
*int*            | *calcAnswer(String statement)* <br> Calculates and executes the best Answer for a given input Statement. <br> **Parameters:** <br> *statement* - your input. <br> **Returns:** <br> ID of the chosen Answer and -1 if no Answer was found.
  
  
  
Note: Debug Mode can be turned on and off if you enter setDebugOn or setDebugOff.
