package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.util.List;

public class SplitPay extends Transaction {

    private String currency;
    private List<String> involvedAccounts;
    private double amount;

    public SplitPay(final int timestamp, final String description, final String currency,
                    final List<String> involvedAccounts, final double amount) {
        super(timestamp, description);
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
        this.amount = amount;
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
        result.put("currency", currency);
        result.put("amount", amount);
        ArrayNode accounts = mapper.createArrayNode();
        for (String account : involvedAccounts) {
            accounts.add(account);
        }
        result.put("involvedAccounts", accounts);
        return result;
    }

}
