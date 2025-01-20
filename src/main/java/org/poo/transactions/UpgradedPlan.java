package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UpgradedPlan extends Transaction {

    private String account, newPlan;

    public UpgradedPlan(final int timestamp, final String account, final String description,
                        final String newPlan) {
        super(timestamp, description);
        this.account = account;
        this.newPlan = newPlan;
    }

    @Override
    public ObjectNode print(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();
        result.put("accountIBAN", account);
        result.put("newPlanType", newPlan);
        result.put("description", getDescription());
        result.put("timestamp", getTimestamp());
        return result;
    }
}
