package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class InterestIncome extends Transaction {

    private String currency;
    private double amount;

    public InterestIncome(final int timestamp, final String description, final String currency,
                          final double amount) {
        super(timestamp, description);
        this.currency = currency;
        this.amount = amount;
    }
    @Override
    public ObjectNode print(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();
        result.put("description", getDescription());
        result.put("currency", currency);
        result.put("amount", amount);
        result.put("timestamp", getTimestamp());
        return result;
    }

}
