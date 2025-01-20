package org.poo.transactions;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WithdrawCash extends Transaction {

    public double amount;

    public WithdrawCash(final int timestamp, final String description, final double amount) {
        super(timestamp, description);
        this.amount = amount;
    }

    @Override
    public ObjectNode print(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();
        result.put("amount", amount);
        result.put("description", getDescription());
        result.put("timestamp", getTimestamp());
        return result;
    }
}
