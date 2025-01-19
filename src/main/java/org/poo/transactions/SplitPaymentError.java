package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public class SplitPaymentError extends Transaction {

    private double amount;
    private String currency, brokeOne;
    private List<String> involvedAccounts;

    public SplitPaymentError(final int timestamp, final String description, final double amount,
                             final String currency, final List<String> involvedAccounts,
                             final String brokeOne) {
        super(timestamp, description);
        this.amount = amount;
        this.currency = currency;

        this.involvedAccounts = involvedAccounts;
        this.brokeOne = brokeOne;
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
        result.put("error", "Account " + brokeOne + " has insufficient funds for a split payment.");
        return result;
    }

}
