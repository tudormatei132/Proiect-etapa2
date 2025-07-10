
# Proiect Etapa 2 - J. POO Morgan Chase & Co.

## Short description of the app
	
The application emulates the "back-end" of a simple banking application, giving the results
of a series of commands in JSON format.

The input is given in the JSON format and the results will be printed into files in the same format. For the managing the input and printing the output, the Object Mapper, ArrayNode, ObjectNode and ObjectWritter classes from the com.fasterxml.jackson.databind library were used. The class that represents objects that will be printed will also have a method which will return an ObjectNode that contains the needed stats which will later be added to the output ArrayNode.


## Design patterns

I've used the following design patterns:
* Command, to make the implementation of different actions easier (by separating each command, it was
much easier to think about a solution, only working with what you need)
* Factory Method, used to get the corresponding command using a switch case
It was really easy to add new Commands and execute them thanks to this pattern
* Builder, used to create error logs which were printed on the screen (in the class Log)
I decided to use Builder here since a lot of those messages had many optional fields, so it made it
easier to create and print messages
* Singleton, used in the implementation of the Converter. There is no need for 2 Converters, and if I created
another one by accident, it would've been that nice.
* Observer, used in the Split Payment part. the SplitCustom class communicates with the involved accounts
and sends them updates whenever something happens with the payment.

## Classes

### Account Package

Contains the following classes:

#### User

- stores information about users (accounts, name, email, transactions, aliases)
- is also used to print the information about the user with  the print method
- also contains a method used to look up after new classic accounts that use a certain currency, so you
can withdraw money from your savings account
#### Account

- used to store information about accounts (IBAN, owner, currency, balance, cards, transactions)
- has methods to add/remove cards from the list of cards 
- used to get payments and transactions between given timestamps
- also has a setInterestRate method that returns -1
- now also stores info about the permanent discount (from "spendingsThreshold") and the one time discounts
- the one time discounts are implemented using 3 3 element arrays which represent:
the discounts that the account gained, the fact if they were used or not before, and another array that specificies that the discount had just been gained and the user needs to wait one more transaction to use them
##### ClassicAccount
- extends the Account class
##### SavingsAccount
- extends the Account class and implements the setInterestRate to signal that the command can be applied on that account
##### BusinessAccount
- extends the Account class, but it has some unique variables to keep track of transactions in the business report
#### In this package, there were also added some classes that help keep track of the spendings of a business
Those are: CommerciantTotal, CommerciantRecord, SimpleRecord, Pair. 
- The first one is used to link a commerciant to the employees that have made transactions to it
- The second one is to link a commerciant to an employee (a single transaction)
- The third one is a simple transfer where the User and the amount are kept
All of them are used for either printing or iterating through available transactions (making it much easier).

#### Card
- stores information about the card: status, number and corresponding account
- has method to print the card; to use the card, which extracts money from the account
- also has an update() method which will check if the current status of the card is still correct 

##### OneTimeCard
- extends the Card class and it represent a card that can only be used once
- after it's used it will get a new card number
- the need to change the card number is signaled in the use() method, where the status becomes "mustBeReplaced"


###  Commands package
This is where the command pattern was used, but without an invoker and a history of commands, because they were not needed.
#### Command interface
- has the void execute() method, which will be used by the command classes to implement the action
- the other classes from this package implement this interface, which will containt the implementation of each command from the input, except the next class
#### CommandHandler class
- has a method to get the right type of instace for the command, then executes it
- the method used to get the right type of command implements the Factory pattern, with a switch, returning a new instance of a class that implements the Command interface, based on input

### Errors package
- contains a Log class that implements the Builder pattern
- has a method print() that is used to add information about the error in the output
- the method check which fields were initialiased and adds them to output

### Transactions package
- has multiple classes that represents different type of transactions, which will be stored in the Account class and in the User class
- each class has a print method() that will be used when iterating through arrays of transaction to print them with ease
### System package
#### Converter class
- implements the Singleton pattern, since we only need one instance of the converter
- it uses a Graph with edges, both classes defined in the graph package
- it has 2 important methods, a dfs() method that does a Depth-First Search through the Currency Graph and returns the conversion rate between 2 given currencies, recursively
- the other method defines a set of visited nodes and returns the conversion rate given by the DFS
#### SystemManager class
- contains an ArrayList of Users, 3 HashMaps, one to associate 	an IBAN string to an Account instance, another one to associate an email with an User instance and the last one to associate a card number to a Card instance and a Converter (which will be the only instance of the Converter)
- it contains only one method, run(), that extracts the Users and the exchange rates from the input and adds them in the ArrayList of Users and in the currency graph, respectively. Then it creates a CommandHandler instance, which will be used to execute every command from the input. At last, it will reset the converter for the next test. 
#### SplitCustom
- contains the details about a Split Payment
- has 2 main methods, accept() and reject(), which will accept a payment and check if the conditions were met for 
the transaction to be finalized and, respectively, reject a payment and remove it from everyone's queue of requests. Both methods will call notify() to update the accounts about the state
### Utils package
In the Utils class, I've added 2 functions: one to get the commission and another one to get the difference in
years between the current date and a given one.
Also, I've created another class here, AutoUpgrader, which checks if a silver account can be upgraded, automatically, to gold, by checking the number of transactions.

### Commerciant package
#### Commerciant class
- stores info about a commerciant
-  is an abstract class, the setCashBack method being implemented in a different way depending on the cashback strategy
##### SpendingsCommerciant class
- implements the abstract method by checking whether certain transaction marks were reached
- will return the cashback based on the amount on money spent and the user's plan
##### TransactionsCommerciant class
- similar to the previous class
- also extends the Commerciant class
- instead of relying on the amount spent, it will just check how many transactions were made and give a specific
discount
