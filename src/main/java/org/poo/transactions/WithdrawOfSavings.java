package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WithdrawOfSavings extends Transaction {


    private double amount;
    private String classicIban, savingsIban;
    public WithdrawOfSavings(double amount, int timestamp,
                             String classicIban, String savingsIban) {
        super(timestamp, "Savings withdrawal");
        this.amount = amount;

        this.classicIban = classicIban;
        this.savingsIban = savingsIban;
    }

    public ObjectNode print(ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();
        result.put("amount", amount);
        result.put("classicAccountIBAN", classicIban);
        result.put("savingsAccountIBAN", savingsIban);
        result.put("timestamp", getTimestamp());
        result.put("description", getDescription());
        return result;
    }

}
