package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CardCreation extends Transaction {
    private String card, cardHolder, account;

    public CardCreation(final int timestamp, final String card, final String cardHolder,
                        final String account) {
        super(timestamp, "New card created");
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }

    /**
     * will print the details of the transaction
     * @param mapper used to create the ObjectNode
     * @return the node which will be added to the output node
     */
    public ObjectNode print(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();
        result.put("timestamp", getTimestamp());
        result.put("description", getDescription());
        result.put("card", card);
        result.put("cardHolder", cardHolder);
        result.put("account", account);
        return result;
    }
}
