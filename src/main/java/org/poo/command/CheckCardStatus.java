package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.account.Card;
import org.poo.errors.Log;
import org.poo.transactions.Transaction;

public class CheckCardStatus implements Command {

    private Card card;
    private int timestamp;
    private ArrayNode output;
    private ObjectMapper mapper;
    public CheckCardStatus(final Card card, final int timestamp, final ArrayNode output,
                           final ObjectMapper mapper) {
        this.card = card;
        this.timestamp = timestamp;
        this.output = output;
        this.mapper = mapper;
    }


    /**
     * if the card exists, it will update its status based on the account's balance
     */
    public void execute() {

        if (card == null) {
            Log error = new Log.Builder("checkCardStatus", timestamp).
                    setDescription("Card not found").setDetailsTimestamp(timestamp).build();

            output.add(error.print((mapper)));
            return;
        }
        if (card.getStatus().toString().equals("frozen")) {
            return;
        }
        card.update();
        String newStatus = card.getStatus().toString();


        Transaction checkStatus;
        if (newStatus.equals("frozen")) {
            checkStatus = new Transaction(timestamp, "You have reached the minimum amount of "
                    + "funds, the card will be frozen");
            card.getAccount().getTransactions().add(checkStatus);
            card.getAccount().getUser().getTransactions().add(checkStatus);
        }


    }
}
