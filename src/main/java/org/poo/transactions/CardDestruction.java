package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CardDestruction extends CardCreation {

    public CardDestruction(final int timestamp, final String card, final String cardHolder,
                           final String account) {
        super(timestamp, card, cardHolder, account);
        setDescription("The card has been destroyed");
    }
    /**
     * will print the details of the transaction
     * @param mapper used to create the ObjectNode
     * @return the node which will be added to the output node
     */
    public ObjectNode print(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();
        result.put("description", getDescription());
        result.put("card", getCard());
        result.put("cardHolder", getCardHolder());
        result.put("account", getAccount());
        result.put("timestamp", getTimestamp());
        return result;
    }
}
