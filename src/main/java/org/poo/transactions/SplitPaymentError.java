package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public class SplitPaymentError extends Transaction {

    private double amount;
    private String currency, brokeOne;
    private List<String> involvedAccounts;
    private List<Double> amounts;
    private String type;
    private String error;
    public SplitPaymentError(final int timestamp, final String description, final double amount,
                             final String currency, final List<String> involvedAccounts,
                             final String brokeOne, final String type,
                             final List<Double> amounts, final String error) {
        super(timestamp, description);
        this.amount = amount;
        this.currency = currency;

        this.involvedAccounts = involvedAccounts;
        this.brokeOne = brokeOne;
        this.type = type;
        this.amounts = amounts;
        this.error = error;
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
        if (type.equals("equal")) {
            result.put("amount", amount);
        }
        result.put("splitPaymentType", type);
        ArrayNode accounts = mapper.createArrayNode();
        for (String account : involvedAccounts) {
            accounts.add(account);
        }
        result.put("involvedAccounts", accounts);
        if (!type.equals("equal")) {
            ArrayNode amountNode = mapper.createArrayNode();
            for (Double amount : amounts) {
                amountNode.add(amount);
            }
            result.put("amountForUsers", amountNode);
        }
        result.put("error", error);
        return result;
    }

}
