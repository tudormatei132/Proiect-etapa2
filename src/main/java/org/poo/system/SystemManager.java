package org.poo.system;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import org.poo.account.Account;
import org.poo.account.Card;
import org.poo.commerciant.Commerciant;
import org.poo.account.User;
import org.poo.command.CommandHandler;
import org.poo.commerciant.SpendingsCommerciant;
import org.poo.commerciant.TransactionsCommerciant;
import org.poo.fileio.*;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class SystemManager {

    private ArrayList<User> users;
    private HashMap<String, Account> accountMap;
    private HashMap<String, User> userMap;
    private HashMap<String, Card> cardMap;
    private HashMap<String, Commerciant> commerciantMap;
    private final Converter converter;


    public SystemManager() {
        users = new ArrayList<>();
        accountMap = new HashMap<>();
        userMap = new HashMap<>();
        cardMap = new HashMap<>();
        converter = Converter.getInstance();
        commerciantMap = new HashMap<>();
    }


    /**
     * this is the main method of the program, it basically gets the basic information of the input
     * then it starts passing commands to the CommandHandler
     * @param input the given input which will be used to extract users, rates and the commands
     * @param mapper the mapper used to create different nodes for printing
     * @param output the output node that will be printed after execution
     */
    public void run(final ObjectInput input, final ObjectMapper mapper, final ArrayNode output) {
        for (UserInput user : input.getUsers()) {
            User toBeAdded = new User(user);
            users.add(toBeAdded);
            userMap.put(toBeAdded.getEmail().toString(), toBeAdded);
        }

        for (ExchangeInput exchange : input.getExchangeRates()) {
            converter.addNewRate(exchange.getFrom(), exchange.getTo(), exchange.getRate());
        }

        for (CommerciantInput commerciant : input.getCommerciants()) {
            Commerciant toBeAdded;
            if (commerciant.getCashbackStrategy().equals("nrOfTransactions")) {
                toBeAdded = new TransactionsCommerciant(commerciant);
            } else {
                toBeAdded = new SpendingsCommerciant(commerciant);
            }
            commerciantMap.put(commerciant.getCommerciant(), toBeAdded);
            commerciantMap.put(commerciant.getAccount(), toBeAdded);
        }

        CommandHandler handler = new CommandHandler(this, mapper, output);

        for (CommandInput command : input.getCommands()) {
            handler.execute(command);
        }

        converter.reset();
    }

}
