package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.User;
import org.poo.errors.Log;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Collections;

public class PrintTransactions implements Command {

    private User user;
    private ArrayNode output;
    private ObjectMapper mapper;
    private int timestamp;
    public PrintTransactions(final User user, final ArrayNode output, final ObjectMapper mapper,
                             final int timestamp) {
        this.user = user;
        this.output = output;
        this.mapper = mapper;
        this.timestamp = timestamp;
    }

    /**
     * checks if the user exists in the database and print the transactions made by them
     */
    public void execute() {
        if (user == null) {
            Log error = new Log.Builder("printTransactions", timestamp)
                            .setDetailsTimestamp(timestamp)
                            .setError("User not found").build();

            output.add(error.print(mapper));
            return;
        }
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "printTransactions");

        ArrayNode transactions = mapper.createArrayNode();

        Collections.sort(user.getTransactions());

        for (Transaction transaction : user.getTransactions()) {
            transactions.add(transaction.print(mapper));
        }

        node.put("output", transactions);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
